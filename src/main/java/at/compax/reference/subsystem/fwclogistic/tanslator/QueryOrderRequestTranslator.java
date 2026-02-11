package at.compax.reference.subsystem.fwclogistic.tanslator;

import org.springframework.stereotype.Component;

import at.compax.foundation.subsystem.api.model.v2.ValuesConsumer;
import at.compax.reference.subsystem.fwclogistic.constants.Constants;
import at.compax.reference.subsystem.fwclogistic.model.request.QueryOrderRequest;

@Component
public class QueryOrderRequestTranslator {

  public QueryOrderRequest translate(ValuesConsumer consumer) {

    return QueryOrderRequest.builder()
        .clientId(consumer.longValue(Constants.CLIENT_ID))
        .referenceNum(consumer.stringValue(Constants.IF_TRANSACTION_ID))
        .build();
  }
}
