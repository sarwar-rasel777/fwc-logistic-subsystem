package at.compax.reference.subsystem.fwclogistic;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;

import at.compax.reference.subsystem.fwclogistic.config.ApplicationConfig;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
@Import(ApplicationConfig.class)
@PropertySource(value = "classpath:subsystem.properties")
public class FwcLogisticSubsystemApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(FwcLogisticSubsystemApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void addGitInfoMetric() {
    try {
      Gauge.Builder<Supplier<Number>> builder = Gauge.builder("git_info", () -> 1).description("Information about the last commit");

      Properties prop = new Properties();
      prop.load(FwcLogisticSubsystemApplication.class.getClassLoader().getResourceAsStream("git.properties"));
      for (Map.Entry<Object, Object> entry : prop.entrySet()) {
        String key = (String) entry.getKey();
        String value = (String) entry.getValue();
        builder.tag(key, value);
      }

      builder.register(Metrics.globalRegistry);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
