package at.compax.reference.subsystem.fwclogistic.model.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
  private String grant_type;
  private String refresh_token;
  private String client_id;
  private String client_secret;
}