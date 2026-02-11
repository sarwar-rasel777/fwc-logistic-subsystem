package at.compax.reference.subsystem.fwclogistic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import at.compax.foundation.subsystem.service.config.SubsystemServiceConfig;
import at.compax.reference.oauth.OAuth2TokenService;
import lombok.RequiredArgsConstructor;

@Configuration
@Import({ SubsystemServiceConfig.class, RestTemplateConfig.class })
@RequiredArgsConstructor
public class ApplicationConfig {

  @Value("${gateway.token.url:[unset]}")
  String tokenUrl;

  @Value("${gateway.client.id:[unset]}")
  String clientId;

  @Value("${gateway.client.secret:[unset]}")
  String clientSecret;

  @Value("${todoclientname.todorestsubsystemname.scope:}")
  String scope;

  @Bean
  public OAuth2TokenService oAuth2TokenService() {
    return new OAuth2TokenService(tokenUrl, clientId, clientSecret, scope);
  }
}
