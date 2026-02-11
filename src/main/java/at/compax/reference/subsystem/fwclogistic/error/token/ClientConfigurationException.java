package at.compax.reference.subsystem.fwclogistic.error.token;

import lombok.Getter;

@Getter
public class ClientConfigurationException extends TokenException {
  private final Long clientId;

  public ClientConfigurationException(Long clientId, String message) {
    super(formatMessage(clientId, message));
    this.clientId = clientId;
  }

  private static String formatMessage(Long clientId, String message) {
    return String.format("Client configuration error for clientId [%s]: %s", clientId, message);
  }
}