package at.compax.reference.subsystem.fwclogistic.error.token;

import lombok.Getter;

@Getter
public class TokenAcquisitionException extends TokenException {
  private final Long clientId;
  private final int httpStatus;

  public TokenAcquisitionException(Long clientId, String message) {
    super(formatMessage(clientId, message));
    this.clientId = clientId;
    this.httpStatus = -1;
  }

  public TokenAcquisitionException(Long clientId, String message, int httpStatus) {
    super(formatMessage(clientId, message, httpStatus));
    this.clientId = clientId;
    this.httpStatus = httpStatus;
  }

  public TokenAcquisitionException(Long clientId, String message, Throwable cause) {
    super(formatMessage(clientId, message), cause);
    this.clientId = clientId;
    this.httpStatus = -1;
  }

  private static String formatMessage(Long clientId, String message) {
    return String.format("Token acquisition failed for clientId [%s]: %s", clientId, message);
  }

  private static String formatMessage(Long clientId, String message, int httpStatus) {
    return String.format("Token acquisition failed for clientId [%s] with HTTP status %d: %s",
        clientId, httpStatus, message);
  }
}
