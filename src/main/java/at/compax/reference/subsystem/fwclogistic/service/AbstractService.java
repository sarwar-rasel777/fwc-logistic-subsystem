package at.compax.reference.subsystem.fwclogistic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import at.compax.foundation.subsystem.service.component.model.ValuesBuilderFactory;
import at.compax.foundation.subsystem.service.component.model.ValuesConsumerFactory;
import at.compax.reference.subsystem.fwclogistic.config.ItosConfig;
import at.compax.reference.subsystem.fwclogistic.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractService {

  @Autowired
  @Qualifier("authenticatedRestTemplate")
  private RestTemplate template;

  @Autowired
  protected ItosConfig itosConfig;
  @Autowired
  protected ValuesConsumerFactory consumerFactory;
  @Autowired
  protected ValuesBuilderFactory builderFactory;

  @Value("${fwc.logistic.base.url}")
  protected String fwclogisticBaseUrl;

  HttpHeaders getHeaders(Long clientId) {
    return ApiUtils.getHeaders(clientId);
  }

  <T, V> ResponseEntity<V> execute(String uri, HttpMethod method, HttpEntity<T> entity, Class<V> responseType) {
    printApiCallingLog(uri, method, entity);
    return template.exchange(uri, method, entity, responseType);
  }

  <T> void printApiCallingLog(String uri, HttpMethod method, HttpEntity<T> entity) {
    log.info("Calling [{}] API [{}] | Headers: [{}] | Body: [{}]",
        method,
        uri,
        entity.getHeaders(),
        entity.getBody() != null ? entity.getBody() : "no body"
    );
  }

}
