package at.compax.reference.subsystem.fwclogistic.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static at.compax.reference.subsystem.fwclogistic.constants.Constants.OPEN_API_GROUP;

@Configuration
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi openApiGroup() {
    return GroupedOpenApi.builder()
        .group(OPEN_API_GROUP)
        .packagesToScan("at.compax.reference.subsystem.fwclogistic")
        .build();
  }
}
