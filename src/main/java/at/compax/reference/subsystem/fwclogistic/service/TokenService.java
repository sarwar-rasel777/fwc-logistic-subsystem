package at.compax.reference.subsystem.fwclogistic.service;

import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import at.compax.reference.subsystem.fwclogistic.config.ItosConfig;
import at.compax.reference.subsystem.fwclogistic.error.token.TokenAcquisitionException;
import at.compax.reference.subsystem.fwclogistic.error.token.TokenException;
import at.compax.reference.subsystem.fwclogistic.model.token.TokenResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  protected ItosConfig itosConfig;

  @Value("${fwc.simulation:true}")
  private boolean simulation;

  @Value("${fwc.logistic.base.url}")
  private String fwclogisticBaseUrl;

  @Value("${fwc.logistic.client-identifier}")
  private String defaultClientIdentifier;

  @Value("${fwc.logistic.client-secret}")
  private String defaultClientSecret;

  private TokenResponse currentToken;

  private static final Integer EXPIRES_IN_SECONDS = 3600; // 1 hour

  public synchronized TokenResponse getTokens(Long clientId) {

    if (currentToken == null || currentToken.isExpired()) {
      if (simulation) {
        currentToken = generateFakeToken(clientId);
      } else {
        currentToken = obtainNewToken(clientId, currentToken != null ? currentToken.getRefreshToken() : null);
      }
    }

    return currentToken;
  }

  private TokenResponse generateFakeToken(Long clientId) {

    long now = Instant.now().getEpochSecond();

    TokenResponse token = new TokenResponse();
    token.setAccessToken("fake-access-token-" + UUID.randomUUID());
    token.setRefreshToken("fake-refresh-token-" + UUID.randomUUID());
    token.setTokenType("Bearer");
    token.setExpiresIn(EXPIRES_IN_SECONDS);
    token.setObtainedAt(now);
    token.setFwcClientId("fwc-client-" + clientId);

    log.info(
        "Simulated FWC token generated for client {} (expires in {} seconds)",
        clientId,
        EXPIRES_IN_SECONDS
    );

    return token;
  }

  private TokenResponse obtainNewToken(Long clientId, String refreshToken) {

    String tokenUrl = fwclogisticBaseUrl + "/auth/o2/token";
    String clientIdentifier = defaultClientIdentifier;
    String clientSecret = defaultClientSecret;

    if (clientId != null && !fwclogisticBaseUrl.contains("localhost")) {
      try {
        tokenUrl = itosConfig.getfwclogisticBaseUri(clientId) + "/auth/o2/token";
        clientIdentifier = itosConfig.getFwcClientIdentifier(clientId);
        clientSecret = itosConfig.getFwcClientSecret(clientId);
      } catch (Exception e) {
        log.warn("Could not get ITOS config for client {}, using defaults", clientId);
      }
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    if (refreshToken != null) {
      form.add("grant_type", "refresh_token");
      form.add("refresh_token", refreshToken);
    } else {
      form.add("grant_type", "client_credentials");
    }
    form.add("client_id", clientIdentifier);
    form.add("client_secret", clientSecret);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

    try {
      log.debug("Sending token request to: {}", tokenUrl);

      ResponseEntity<TokenResponse> response =
          restTemplate.postForEntity(tokenUrl, request, TokenResponse.class);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        TokenResponse newTokenResponse = response.getBody();
        newTokenResponse.setObtainedAt(System.currentTimeMillis() / 1000);
        newTokenResponse.setFwcClientId(clientIdentifier);

        // If expiresIn is large, assume it's in ms and convert to seconds
        if (newTokenResponse.getExpiresIn() != null && newTokenResponse.getExpiresIn() > 10000) {
           newTokenResponse.setExpiresIn(newTokenResponse.getExpiresIn() / 1000);
        }

        log.info("Fwc token acquired/refreshed. Expires in {} seconds",
            newTokenResponse.getExpiresIn());
        return newTokenResponse;
      } else {
        throw new TokenAcquisitionException(
            clientId,
            "Failed to obtain token. HTTP Status: " + response.getStatusCode(),
            response.getStatusCode().value()
        );
      }
    } catch (Exception e) {
      log.error("Error obtaining FWC token from {}", tokenUrl, e);
      throw new TokenAcquisitionException(clientId, "Error obtaining Fwc token", e);
    }
  }

}
