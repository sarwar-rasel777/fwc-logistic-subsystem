package at.compax.reference.subsystem.fwclogistic.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.compax.foundation.subsystem.api.model.v2.PayloadCreationModel;
import at.compax.foundation.subsystem.api.model.v2.PayloadCreationResponseModel;
import at.compax.foundation.subsystem.api.model.v2.PayloadSendingModel;
import at.compax.foundation.subsystem.api.model.v2.PayloadSendingResponseModel;
import at.compax.foundation.subsystem.api.model.v2.ValuesBuilder;
import at.compax.foundation.subsystem.service.component.service.SubsystemService;
import at.compax.reference.subsystem.fwclogistic.constants.ResponseConstants;
import at.compax.reference.subsystem.fwclogistic.generator.Generator;
import at.compax.reference.subsystem.fwclogistic.model.request.SendOrderRequest;
import at.compax.reference.subsystem.fwclogistic.tanslator.OrderRequestTranslator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("sendOrderService")
@RequiredArgsConstructor
@Slf4j
public class SendOrderService extends AbstractService implements SubsystemService {

  private final OrderRequestTranslator orderRequestTranslator;
  private final Generator generator;
  private final ObjectMapper mapper;

  @Override
  public PayloadCreationResponseModel createRequest(PayloadCreationModel model) {
    log.info("Calling create request with the payload {} ", model.toString());
    try {
      final SendOrderRequest request = orderRequestTranslator.translate(consumerFactory.consumer(model.getValues()));
      log.info("Translated payload {} ", request.toString());
      return generator.generate(request);
    } catch (Exception e) {
      e.printStackTrace();
      return new PayloadCreationResponseModel(e.getMessage().getBytes());
    }
  }

  @Override
  public PayloadSendingResponseModel sendRequest(PayloadSendingModel model) {
    ValuesBuilder valuesBuilder = builderFactory.builder();

    try {
      final SendOrderRequest request = generator.read(model, SendOrderRequest.class);
      boolean isSimulation = itosConfig.isSimulationEnabled(request.getClientId());
      log.info("FWC SendOrder | Simulation={} | Payload={}", isSimulation, request);

      PayloadSendingResponseModel response = isSimulation
          ? simulateFwcSendOrder(request, valuesBuilder)
          : callFwcSendOrder(request, valuesBuilder);

      return response;

    } catch (Exception ex) {
      log.error("FWC SendOrder failed", ex);

      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, ex.getMessage());

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }
  }

  private PayloadSendingResponseModel simulateFwcSendOrder(
      SendOrderRequest request,
      ValuesBuilder valuesBuilder
  ) {
    PayloadSendingResponseModel responseModel = new PayloadSendingResponseModel();

    // client validation
    if (request.getClientId() == null) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "Client not found");
      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // reference number validation
    if (request.getReferenceNum() == null || request.getReferenceNum().isBlank()) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "Invalid reference number");

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // shipping address validation
    if (request.getShipTo() == null) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "Shipping address missing");

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // order items validation
    if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "No order items");

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // quantity validation
    boolean invalidQty = request.getOrderItems().stream()
        .anyMatch(item -> item.getQty() <= 0);

    if (invalidQty) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "Invalid quantity");

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // success
    valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_SUCCESS);
    valuesBuilder.addValue(ResponseConstants.MESSAGE, "Send order successful");

    return new PayloadSendingResponseModel()
        .values(valuesBuilder.toValues());
  }




  private PayloadSendingResponseModel callFwcSendOrder(SendOrderRequest request, ValuesBuilder valuesBuilder) {
    String uri = fwclogisticBaseUrl + "/rels/orders/item";
    HttpHeaders headers = getHeaders(request.getClientId());
    HttpEntity<SendOrderRequest> entity = new HttpEntity<>(request, headers);

    try {
      ResponseEntity<Map> response = execute(uri, HttpMethod.POST, entity, Map.class);
      if (response.getStatusCode().is2xxSuccessful()) {
        valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_SUCCESS);
        valuesBuilder.addValue(ResponseConstants.MESSAGE, "Send order successful");
      } else {
        valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
        valuesBuilder.addValue(ResponseConstants.MESSAGE, "FWC API error: " + response.getStatusCode());
      }
    } catch (Exception e) {
      log.error("Error calling FWC API", e);
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, e.getMessage());
    }

    return new PayloadSendingResponseModel()
        .values(valuesBuilder.toValues());
  }

}
