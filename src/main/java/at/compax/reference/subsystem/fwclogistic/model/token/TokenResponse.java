package at.compax.reference.subsystem.fwclogistic.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TokenResponse {
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private Integer expiresIn;

  @JsonProperty("refresh_expires_in")
  private Integer refreshExpiresIn;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("token_type")
  private String tokenType;

  private Long obtainedAt;

  private String fwcClientId;

  public boolean isExpired() {
    if (obtainedAt == null || expiresIn == null) return true;
    long currentTime = System.currentTimeMillis() / 1000;
    return (obtainedAt + expiresIn - 30) < currentTime; // 30 seconds buffer
  }

  public boolean isRefreshExpired() {
    if (obtainedAt == null || refreshExpiresIn == null) return true;
    long currentTime = System.currentTimeMillis() / 1000;
    return (obtainedAt + refreshExpiresIn) < currentTime;
  }
}