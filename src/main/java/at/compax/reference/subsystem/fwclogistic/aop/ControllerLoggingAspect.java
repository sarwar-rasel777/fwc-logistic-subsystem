package at.compax.reference.subsystem.fwclogistic.aop;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void restControllerMethods() {
  }

  @Before("restControllerMethods()")
  public void logBefore(JoinPoint joinPoint) {
    if (shouldSkipLogging(joinPoint)) return;
    log.info("API Request for {}.{} with parm{}", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
  }

  @AfterReturning(pointcut = "restControllerMethods()", returning = "result")
  public void logAfterReturning(JoinPoint joinPoint, Object result) {
    if (shouldSkipLogging(joinPoint)) return;
    log.info("API Response of {}.{} is [{}]", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), result);
  }

  @AfterThrowing(pointcut = "restControllerMethods()", throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
    if (shouldSkipLogging(joinPoint)) return;
    log.error("API Error on {}.{} is [{}]", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), ex.getMessage(), ex);
  }

  private boolean shouldSkipLogging(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    return className.contains("org.springdoc") || className.contains("OpenApiResource");
  }
}
