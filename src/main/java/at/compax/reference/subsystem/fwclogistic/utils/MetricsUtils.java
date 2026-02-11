package at.compax.reference.subsystem.fwclogistic.utils;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MetricsUtils {

  public static Counter incrementSenderCounter(String method, String outcome, String exception) {
    Counter counter = Counter.builder("subsystem_sender") //
        .description("TODO") //
        .tag("subsystem", "TODO") //
        .tag("method", method) //
        .tag("outcome", outcome) //
        .tag("exception", exception) //
        .register(Metrics.globalRegistry);
    counter.increment();

    return counter;
  }
}
