package at.compax.reference.subsystem.fwclogistic.error.token;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {
  public TokenException(String message) {
    super(message);
  }

  public TokenException(String message, Throwable cause) {
    super(message, cause);
  }
}