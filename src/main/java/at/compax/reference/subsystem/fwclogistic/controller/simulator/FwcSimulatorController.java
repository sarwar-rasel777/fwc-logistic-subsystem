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
    response.put("totalResults", 1);

    Map<String, Object> embedded = new HashMap<>();
    Map<String, Object> order = new HashMap<>();

    Map<String, Object> readOnly = new HashMap<>();
    readOnly.put("orderId", 1001);
    readOnly.put("asnCandidate", 2);
    readOnly.put("routeCandidate", 3);
    readOnly.put("fullyAllocated", true);
    readOnly.put("importModuleId", 1);
    readOnly.put("exportModuleIds", "standard-export");
    readOnly.put("deferNotification", false);
    readOnly.put("isClosed", true);
    readOnly.put("processDate", "2024-02-11T10:00:00");
    readOnly.put("pickDoneDate", "2024-02-11T11:00:00");
    readOnly.put("pickTicketPrintDate", "2024-02-11T10:30:00");
    readOnly.put("packDoneDate", "2024-02-11T12:00:00");
    readOnly.put("labelsExported", true);
    readOnly.put("invoiceExportedDate", "2024-02-11T12:30:00");
    readOnly.put("invoiceDeliveredDate", "2024-02-11T12:45:00");
    readOnly.put("loadedState", 1); // Loaded
    readOnly.put("loadOutDoneDate", "2024-02-11T13:00:00");
    readOnly.put("reallocatedAfterPickTicketDate", "2024-02-11T10:45:00");
    readOnly.put("routeSent", true);
    readOnly.put("asnSentDate", "2024-02-11T13:30:00");
    readOnly.put("asnSent", true);
    readOnly.put("pkgsExported", true);

    Map<String, Object> customerId = Map.of("externalId", "CUST-001", "name", "Dummy Customer", "id", 3);
    readOnly.put("batchIdentifier", Map.of("nameKey", Map.of("customerIdentifier", customerId, "name", "BATCH-01"), "id", 1));

    Map<String, Object> pkgContent = new HashMap<>();
    pkgContent.put("packageContentId", 1);
    pkgContent.put("packageId", 1);
    pkgContent.put("orderItemId", 101);
    pkgContent.put("receiveItemId", 201);
    pkgContent.put("orderItemPickExceptionId", 0);
    pkgContent.put("qty", 5.0);
    pkgContent.put("lotNumber", "LOT123");
    pkgContent.put("serialNumber", "SN001");
    pkgContent.put("expirationDate", "2025-12-31T23:59:59");
    pkgContent.put("createDate", "2024-02-11T09:00:00");
    pkgContent.put("serialNumbers", java.util.List.of("SN001", "SN002", "SN003", "SN004", "SN005"));

    Map<String, Object> pkg = new HashMap<>();
    pkg.put("packageId", 1);
    pkg.put("packageTypeId", 2);
    pkg.put("packageDefIdentifier", Map.of("name", "Large Box", "id", 2));
    pkg.put("length", 12.0);
    pkg.put("width", 10.0);
    pkg.put("height", 8.0);
    pkg.put("weight", 2.5);
    pkg.put("codAmount", 0.0);
    pkg.put("insuredAmount", 100.0);
    pkg.put("trackingNumber", "TRK7890123456");
    pkg.put("description", "Standard Shipping Box");
    pkg.put("createDate", "2024-02-11T11:45:00");
    pkg.put("oversize", false);
    pkg.put("cod", false);
    pkg.put("ucc128", 1);
    pkg.put("cartonId", "CART-99");
    pkg.put("label", "U2FtcGxlIExhYmVs"); // Base64 for "Sample Label"
    pkg.put("packageContents", java.util.List.of(pkgContent));

    readOnly.put("packages", java.util.List.of(pkg));
    readOnly.put("outboundSerialNumbers", java.util.List.of(Map.of("itemIdentifier", Map.of("sku", "SKU-ABC", "id", 101), "qualifier", "SN", "serialNumbers", java.util.List.of("SN001"))));
    readOnly.put("parcelLabelType", 1); // Pdf
    readOnly.put("customerIdentifier", customerId);
    readOnly.put("facilityIdentifier", Map.of("name", "Main Warehouse", "id", 2));
    readOnly.put("warehouseTransactionSourceType", 7); // RestApi
    readOnly.put("transactionEntryType", 4); // Api
    readOnly.put("importChannelIdentifier", Map.of("name", "Shopify", "id", 2));
    readOnly.put("creationDate", "2024-02-11T08:00:00");
    readOnly.put("createdByIdentifier", Map.of("name", "API User", "id", 10));
    readOnly.put("lastModifiedDate", "2024-02-11T13:45:00");
    readOnly.put("lastModifiedByIdentifier", Map.of("name", "System", "id", 1));
    readOnly.put("status", 1); // Closed

    order.put("readOnly", readOnly);
    order.put("referenceNum", referenceNum);
    order.put("description", "Order for " + referenceNum);
    order.put("poNum", "PO-7788");
    order.put("externalId", "EXT-12345");
    order.put("earliestShipDate", "2024-02-12T08:00:00");
    order.put("shipCancelDate", "2024-02-20T17:00:00");
    order.put("notes", "Please handle with care.");
    order.put("numUnits1", 5.0);
    order.put("unit1Identifier", Map.of("name", "Each", "id", 1));
    order.put("totalWeight", 2.5);
    order.put("totalVolume", 0.5);
    order.put("billingCode", "BILL-01");
    order.put("asnNumber", "ASN-5566");
    order.put("upsServiceOptionCharge", 5.0);
    order.put("upsTransportationCharge", 15.0);
    order.put("addFreightToCod", false);
    order.put("upsIsResidential", true);
    order.put("exportChannelIdentifier", Map.of("name", "Carrier Portal", "id", 5));
    order.put("routePickupDate", "2024-02-11T15:00:00");
    order.put("shippingNotes", "Leave at front door.");

    Map<String, Object> billing = new HashMap<>();
    Map<String, Object> charge = new HashMap<>();
    charge.put("chargeType", 1); // Handling
    charge.put("subtotal", 10.0);
    charge.put("details", java.util.List.of(Map.of(
        "warehouseTransactionPriceCalcId", 1,
        "numUnits", 2.0,
        "chargeLabel", "Handling Fee",
        "unitDescription", "Box",
        "chargePerUnit", 5.0,
        "systemGenerated", true
    )));
    billing.put("billingCharges", java.util.List.of(charge));
    order.put("billing", billing);

    Map<String, Object> shipTo = new HashMap<>();
    shipTo.put("companyName", "Receiver Corp");
    shipTo.put("name", "John Doe");
    shipTo.put("address1", "123 Delivery Lane");
    shipTo.put("city", "Packagetown");
    shipTo.put("state", "CA");
    shipTo.put("zip", "90210");
    shipTo.put("country", "US");
    shipTo.put("emailAddress", "john.doe@example.com");
    order.put("shipTo", shipTo);
    order.put("soldTo", shipTo);
    order.put("billTo", shipTo);

    Map<String, Object> orderItemEmbedded = new HashMap<>();
    Map<String, Object> orderItem = new HashMap<>();
    Map<String, Object> itemReadOnly = new HashMap<>();
    itemReadOnly.put("orderItemId", 101);
    itemReadOnly.put("fullyAllocated", true);
    itemReadOnly.put("unitIdentifier", Map.of("name", "Each", "id", 1));
    itemReadOnly.put("estimatedQty", 5.0);
    itemReadOnly.put("originalPrimaryQty", 5.0);

    Map<String, Object> allocation = new HashMap<>();
    allocation.put("receiveItemId", 201);
    allocation.put("qty", 5.0);
    allocation.put("properlyPickedPrimary", 5.0);
    allocation.put("itemTraits", Map.of("itemIdentifier", Map.of("sku", "SKU-ABC", "id", 101)));

    itemReadOnly.put("allocations", java.util.List.of(allocation));
    orderItem.put("readOnly", itemReadOnly);
    orderItem.put("itemIdentifier", Map.of("sku", "SKU-ABC", "id", 101));
    orderItem.put("qty", 5.0);

    orderItemEmbedded.put("http://api.3plCentral.com/rels/orders/item", java.util.List.of(orderItem));
    order.put("_embedded", orderItemEmbedded);

    embedded.put("http://api.3plCentral.com/rels/orders/order", java.util.List.of(order));
    response.put("_embedded", embedded);

    return ResponseEntity.ok(response);
  }
}
