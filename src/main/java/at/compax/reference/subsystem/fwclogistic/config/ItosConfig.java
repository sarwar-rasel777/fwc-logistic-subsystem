package at.compax.reference.subsystem.fwclogistic.config;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.compax.foundation.subsystem.api.model.v2.ConfigurationModel;
import at.compax.foundation.subsystem.api.model.v2.ValueTypeModel;
import at.compax.foundation.subsystem.api.model.v2.ValuesConsumer;
import at.compax.foundation.subsystem.service.component.model.ValuesConsumerFactory;
import at.compax.foundation.subsystem.service.component.service.CachedSubsystemConfiguration;
import at.compax.reference.subsystem.fwclogistic.constants.ItosConstants;
import at.compax.reference.subsystem.fwclogistic.error.ConfigException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItosConfig {
  private final ValuesConsumerFactory valuesConsumerFactory;
  private final CachedSubsystemConfiguration configuration;

  public String getfwclogisticBaseUri(Long clientId) {
    ValuesConsumer consumer = valuesConsumerFactory.consumer(getConfigs(clientId));
    return consumer.stringValue(ItosConstants.FWC_ENDPOINT);
  }

  public String getFwcClientIdentifier(Long clientId) {
    ValuesConsumer consumer = valuesConsumerFactory.consumer(getConfigs(clientId));
    return consumer.stringValue(ItosConstants.FWC_CLIENT_IDENTIFIER);
  }

  public String getFwcClientSecret(Long clientId) {
    ValuesConsumer consumer = valuesConsumerFactory.consumer(getConfigs(clientId));
    return consumer.stringValue(ItosConstants.FWC_CLIENT_SECRET);
  }

  public String getFwcRefreshToken(Long clientId) {
    ValuesConsumer consumer = valuesConsumerFactory.consumer(getConfigs(clientId));
    return consumer.stringValue(ItosConstants.FWC_REFRESH_TOKEN);
  }

  public String getFwcFulFillmentOrderOutboundEndpoint(Long clientId) {
    ValuesConsumer consumer = valuesConsumerFactory.consumer(getConfigs(clientId));
    return consumer.stringValue(ItosConstants.FWC_FULFILLMENT_ORDER_OUTBOUND_ENDPOINT);
  }

  public boolean isSimulationEnabled(Long clientId) {
    try {
      ValuesConsumer consumer = valuesConsumerFactory.consumer(getConfigs(clientId));
      return consumer.booleanValue(ItosConstants.FWC_SIMULATION);
    } catch (Exception e) {
      log.warn("FWC_SIMULATION not found in ITOS for client {}, defaulting to false", clientId);
      return false;
    }
  }

  private Map<String, ValueTypeModel> getConfigs(Long clientId) {
    ConfigurationModel configModel;
    if (clientId == null) configModel = configuration.getConfiguration();
    else configModel = configuration.getConfiguration(clientId);

    if ((configModel.getConfigs() == null) || (configModel.getConfigs().size() <= 0)) {
      throw new ConfigException("Error retrieving subsystem configuration (No ITOS set for vesta payment subsystem or cannot find Configuration).");
    }
    return configModel.getConfigs();
  }
}
