package at.compax.reference.subsystem.fwclogistic.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

import at.compax.reference.subsystem.fwclogistic.constants.ApiConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiUtils {

  private ApiUtils() {
    throw new IllegalStateException("");
  }

  public static HttpHeaders getHeaders(@Nullable Long clientId) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(ApiConstants.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.set(ApiConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(ApiConstants.X_CLIENT_ID, String.valueOf(clientId));

    return headers;
  }
}
