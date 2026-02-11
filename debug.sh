export SUBSYSTEM_SERVICE_URL=https://ewint-subsystem-service.dev-fra.int.compax.at/rp-subsystem-service/api

mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"