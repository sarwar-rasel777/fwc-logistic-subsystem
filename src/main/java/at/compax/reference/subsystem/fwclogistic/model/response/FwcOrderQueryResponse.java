package at.compax.reference.subsystem.fwclogistic.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FwcOrderQueryResponse {

  private Integer totalResults;

  @JsonProperty("_embedded")
  private EmbeddedOrders embedded;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EmbeddedOrders {
    @JsonProperty("http://api.3plCentral.com/rels/orders/order")
    private List<FwcOrderResponse> orders;
  }
}
