package at.compax.reference.subsystem.fwclogistic.error;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
  public void handleRuntimeExecption(HttpServletResponse response, RuntimeException runtimeException) throws IOException {
    log.error("An error occurred while processing the request. Error message : %s".formatted(runtimeException.getMessage()));
    log.error("", runtimeException);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
}