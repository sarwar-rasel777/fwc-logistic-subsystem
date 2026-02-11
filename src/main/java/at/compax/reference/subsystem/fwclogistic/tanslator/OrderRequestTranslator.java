package at.compax.reference.subsystem.fwclogistic.tanslator;

import java.util.List;
import org.springframework.stereotype.Component;

import at.compax.foundation.subsystem.api.component.translator.v2.Translator;
import at.compax.foundation.subsystem.api.model.v2.ValuesConsumer;
import at.compax.reference.subsystem.fwclogistic.constants.Constants;
import at.compax.reference.subsystem.fwclogistic.model.request.ItemIdentifier;
import at.compax.reference.subsystem.fwclogistic.model.request.OrderItem;
import at.compax.reference.subsystem.fwclogistic.model.request.SendOrderRequest;
import at.compax.reference.subsystem.fwclogistic.model.request.ShipTo;

@Component
public class OrderRequestTranslator extends AbstractTranslator
    implements Translator<SendOrderRequest> {

  @Override
  public SendOrderRequest translate(ValuesConsumer consumer) {

    return SendOrderRequest.builder()
        .clientId(consumer.longValue(Constants.CLIENT_ID))
        .referenceNum(consumer.stringValue(Constants.IF_TRANSACTION_ID))
        .shipTo(buildShipTo(consumer))
        .orderItems(List.of(buildOrderItem(consumer)))
        .customerIdentifier(consumer.stringValue(Constants.CUSTOMER_IDENTIFIER))
        .facilityIdentifier(consumer.stringValue(Constants.FACILITY_IDENTIFIER))
        .build();
  }

  private OrderItem buildOrderItem(ValuesConsumer consumer) {
    ItemIdentifier itemIdentifier = ItemIdentifier.builder()
        .sku(consumer.stringValue(Constants.SKU))
        .build();

    int quantity = resolveQuantity(consumer);

    return new OrderItem(itemIdentifier, quantity);
  }

  private int resolveQuantity(ValuesConsumer consumer) {
    Integer quantity = consumer.valueOptional(Constants.QUANTITY, Integer.class);
    if (quantity != null) {
      return quantity;
    }

    String quantityStr = consumer.stringValue(Constants.QUANTITY);
    try {
      return Integer.parseInt(quantityStr);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(
          "Invalid quantity value: " + quantityStr, ex
      );
    }
  }

  private ShipTo buildShipTo(ValuesConsumer consumer) {
    ShipTo.ShipToBuilder builder = ShipTo.builder()
        .name(consumer.value("name", String.class))
        .address1(consumer.value("address1", String.class))
        .city(consumer.value("city", String.class));

    setIfPresent(builder::address2, consumer.valueOptional("address2", String.class));
    setIfPresent(builder::country, consumer.valueOptional("country", String.class));
    setIfPresent(builder::state, consumer.valueOptional("state", String.class));
    setIfPresent(builder::zip, consumer.valueOptional("zip", String.class));

    return builder.build();
  }

  private void setIfPresent(java.util.function.Consumer<String> setter, String value) {
    if (value != null && !value.isBlank()) {
      setter.accept(value);
    }
  }
}
