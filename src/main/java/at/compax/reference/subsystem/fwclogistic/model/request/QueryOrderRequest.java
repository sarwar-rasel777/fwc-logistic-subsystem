package at.compax.reference.subsystem.fwclogistic.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryOrderRequest{
  private Long clientId;
  private String referenceNum;
}