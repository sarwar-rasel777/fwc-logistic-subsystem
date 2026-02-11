package at.compax.reference.subsystem.fwclogistic.controller.simulator;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.compax.reference.subsystem.fwclogistic.constants.ResponseConstants;
import at.compax.reference.subsystem.fwclogistic.model.request.SendOrderRequest;
import at.compax.reference.subsystem.fwclogistic.model.token.TokenResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/simulator")
@Tag(name = "Fwc Simulator", description = "Simulator for FWC/3PL Central API")
public class FwcSimulatorController {

  @PostMapping(value = "/auth/o2/token", consumes = "application/x-www-form-urlencoded")
  public ResponseEntity<TokenResponse> getToken(
      @RequestParam("grant_type") String grantType,
      @RequestParam(value = "refresh_token", required = false) String refreshToken,
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret
  ) {
    log.info("Simulator: Received token request for client_id: {}", clientId);

    TokenResponse response = new TokenResponse();
    response.setAccessToken("simulated-access-token-" + UUID.randomUUID());
    response.setRefreshToken("simulated-refresh-token-" + UUID.randomUUID());
    response.setTokenType("Bearer");
    response.setExpiresIn(3600000); // 1 hour in ms
    response.setRefreshExpiresIn(86400000); // 24 hours in ms
    response.setObtainedAt(Instant.now().getEpochSecond());
    response.setFwcClientId(clientId);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/rels/orders/item")
  public ResponseEntity<Map<String, Object>> sendOrder(@RequestBody SendOrderRequest request) {
    log.info("Simulator: Received send order request: {}", request);

    Map<String, Object> response = new HashMap<>();
    response.put(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_SUCCESS);
    response.put(ResponseConstants.MESSAGE, "Order created successfully in simulator");
    response.put("referenceNum", request.getReferenceNum());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/orders/{referenceNum}")
  public ResponseEntity<Map<String, Object>> getOrder(@PathVariable String referenceNum) {
    log.info("Simulator: Received query order request for referenceNum: {}", referenceNum);

    Map<String, Object> response = new HashMap<>();
    response.put(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_SUCCESS);
    response.put(ResponseConstants.MESSAGE, "Order found in simulator");
    response.put("referenceNum", referenceNum);
    response.put("orderStatus", "Shipped");

    return ResponseEntity.ok(response);
  }
}
