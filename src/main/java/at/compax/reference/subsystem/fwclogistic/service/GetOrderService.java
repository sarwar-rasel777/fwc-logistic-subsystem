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

import at.compax.reference.subsystem.fwclogistic.constants.Constants;
import at.compax.reference.subsystem.fwclogistic.constants.ResponseConstants;
import at.compax.reference.subsystem.fwclogistic.generator.Generator;
import at.compax.reference.subsystem.fwclogistic.model.request.QueryOrderRequest;
import at.compax.reference.subsystem.fwclogistic.tanslator.QueryOrderRequestTranslator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("getOrderService")
@RequiredArgsConstructor
@Slf4j
public class GetOrderService extends AbstractService implements SubsystemService {

  private final QueryOrderRequestTranslator requestTranslator;
  private final Generator generator;
  private final ObjectMapper mapper;

  @Override
  public PayloadCreationResponseModel createRequest(PayloadCreationModel model) {
    log.info("Calling create request with the payload {}", model);
    try {
      QueryOrderRequest request = requestTranslator.translate(consumerFactory.consumer(model.getValues()));

      log.info("Translated payload {}", request);
      return generator.generate(request);

    } catch (Exception e) {
      log.error("Error during request creation", e);
      return new PayloadCreationResponseModel(e.getMessage().getBytes());
    }
  }

  @Override
  public PayloadSendingResponseModel sendRequest(PayloadSendingModel model) {

    ValuesBuilder valuesBuilder = builderFactory.builder();

    try {
      QueryOrderRequest request = generator.read(model, QueryOrderRequest.class);
      boolean isSimulation = itosConfig.isSimulationEnabled(request.getClientId());
      log.info("FWC QueryOrder | Simulation={} | Payload={}", isSimulation, request);

      return isSimulation
          ? simulateFwcQueryOrder(request, valuesBuilder)
          : callFwcGetOrder(request, valuesBuilder);

    } catch (Exception ex) {
      log.error("FWC QueryOrder failed", ex);

      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, ex.getMessage());

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }
  }

  private PayloadSendingResponseModel simulateFwcQueryOrder(
      QueryOrderRequest request,
      ValuesBuilder valuesBuilder
  ) {

    // client validation
    if (request.getClientId() == null) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "Client not found");

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // reference validation
    if (request.getReferenceNum() == null || request.getReferenceNum().isBlank()) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "Invalid reference number");

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // simulate order not found
    if (request.getReferenceNum().startsWith("404")) {
      valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_FAIL);
      valuesBuilder.addValue(ResponseConstants.MESSAGE, "Order not found");

      return new PayloadSendingResponseModel()
          .values(valuesBuilder.toValues());
    }

    // success
    valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_SUCCESS);
    valuesBuilder.addValue(ResponseConstants.MESSAGE, "Query order successful");

    return new PayloadSendingResponseModel()
        .values(valuesBuilder.toValues());
  }



  private PayloadSendingResponseModel callFwcGetOrder(QueryOrderRequest request, ValuesBuilder valuesBuilder) {
    String uri = fwclogisticBaseUrl + "/orders/" + request.getReferenceNum();
    HttpHeaders headers = getHeaders(request.getClientId());
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<Map> response = execute(uri, HttpMethod.GET, entity, Map.class);
      if (response.getStatusCode().is2xxSuccessful()) {
        valuesBuilder.addValue(ResponseConstants.STATUS, ResponseConstants.RETURN_CODE_SUCCESS);
        valuesBuilder.addValue(ResponseConstants.MESSAGE, "Query order successful");
        if (response.getBody() != null) {
          valuesBuilder.addValue(Constants.VALUE, mapper.writeValueAsString(response.getBody()));
        }
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
