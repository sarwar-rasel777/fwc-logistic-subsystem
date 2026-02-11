package at.compax.reference.subsystem.fwclogistic.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipTo {

  private String companyName;
  private String name;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String zip;
  private String country;
}