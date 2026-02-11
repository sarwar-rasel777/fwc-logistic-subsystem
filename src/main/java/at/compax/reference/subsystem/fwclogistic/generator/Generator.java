package at.compax.reference.subsystem.fwclogistic.generator;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.compax.foundation.subsystem.api.model.v2.PayloadCreationResponseModel;
import at.compax.foundation.subsystem.api.model.v2.PayloadSendingModel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Component
@AllArgsConstructor
public class Generator {

  ObjectMapper mapper;

  @SneakyThrows
  public <T> PayloadCreationResponseModel generate(final T object) {
    return new PayloadCreationResponseModel().payload(mapper.writeValueAsBytes(object));
  }

  @SneakyThrows
  public <T> T read(final PayloadSendingModel model, Class<T> clazz) {
    return mapper.readValue(model.getPayload(), clazz);
  }
}
