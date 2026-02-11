package at.compax.reference.subsystem.fwclogistic.error;

import java.io.Serial;

import lombok.ToString;

@ToString(callSuper = true)
public class ConfigException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public ConfigException(String message) {
    super(message);
  }

}

