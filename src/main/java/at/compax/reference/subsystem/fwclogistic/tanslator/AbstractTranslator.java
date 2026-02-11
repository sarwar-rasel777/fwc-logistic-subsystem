package at.compax.reference.subsystem.fwclogistic.tanslator;

import java.util.Set;
import java.util.function.Consumer;

import at.compax.foundation.subsystem.api.model.v2.ValuesConsumer;

public abstract class AbstractTranslator {

  protected void setIfKeyExists(ValuesConsumer consumer, Set<String> keys, String key, Consumer<String> setter) {
    if (keys.contains(key) && consumer.valueOptional(key, String.class) != null) {
      String value = consumer.stringValue(key);
      setter.accept(value);
    }
  }
}
