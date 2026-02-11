package at.compax.reference.subsystem.fwclogistic.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOrderRequest {

  private Long clientId;
  private String referenceNum;
  private ShipTo shipTo;
  private List<OrderItem> orderItems;
  private String customerIdentifier;
  private String facilityIdentifier;

}
