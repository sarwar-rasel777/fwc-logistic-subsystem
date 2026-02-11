package at.compax.reference.subsystem.fwclogistic.error.token;

import lombok.Getter;

@Getter
public class TokenRefreshException extends TokenException {
  private final Long clientId;

  public TokenRefreshException(Long clientId, String message) {
    super(formatMessage(clientId, message));
    this.clientId = clientId;
  }

  public TokenRefreshException(Long clientId, String message, Throwable cause) {
    super(formatMessage(clientId, message), cause);
    this.clientId = clientId;
  }

  private static String formatMessage(Long clientId, String message) {
    return String.format("Token refresh failed for clientId [%s]: %s", clientId, message);
  }
}