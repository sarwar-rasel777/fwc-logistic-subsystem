package at.compax.reference.subsystem.fwclogistic.config;

import java.io.IOException;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

import at.compax.reference.subsystem.fwclogistic.constants.ApiConstants;
import at.compax.reference.subsystem.fwclogistic.model.token.TokenResponse;
import at.compax.reference.subsystem.fwclogistic.service.TokenService;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class InterceptorConfig {

  @Bean("authenticatedRestTemplate")
  public RestTemplate authenticatedRestTemplate(RestTemplate restTemplate, TokenService tokenService) {
    // Create a copy of the existing restTemplate and add interceptor
    RestTemplate authRestTemplate = new RestTemplate();
    authRestTemplate.setRequestFactory(restTemplate.getRequestFactory());
    authRestTemplate.setMessageConverters(restTemplate.getMessageConverters());
    authRestTemplate.setInterceptors(Collections.singletonList(new BearerTokenInterceptor(tokenService)));
    return authRestTemplate;
  }

  private record BearerTokenInterceptor(TokenService tokenService) implements ClientHttpRequestInterceptor {
    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
      try {
        TokenResponse tokenResponse = tokenService.getTokens(extractClientIdFromRequest(request));
        String token = tokenResponse.getAccessToken();

        if (token != null && !token.isBlank()) {
          request.getHeaders().setBearerAuth(tokenResponse.getRefreshToken());
          request.getHeaders().add("x-fwc-access-token", tokenResponse.getAccessToken());
          request.getHeaders().add("X_CLIENT_ID", tokenResponse.getFwcClientId());
          request.getHeaders().add("X-FWC-CLIENT-ID", tokenResponse.getFwcClientId());

          log.debug("Added bearer token for request to: {}", request.getURI());
        } else {
          log.warn("No valid token available for request to: {}", request.getURI());
        }
      } catch (Exception e) {
        log.warn("Failed to retrieve token for request to: {}, proceeding without token", request.getURI(), e);
      }

      return execution.execute(request, body);
    }

    private Long extractClientIdFromRequest(HttpRequest request) {
      String clientIdHeader = request.getHeaders().getFirst(ApiConstants.X_CLIENT_ID);
      if (clientIdHeader != null) {
        try {
          return Long.parseLong(clientIdHeader);
        } catch (NumberFormatException e) {
          log.warn("Invalid clientId in header: {}", clientIdHeader);
        }
      }
      return null;
    }
  }
}