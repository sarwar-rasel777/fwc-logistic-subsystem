ARG AAX_VERSION=trunk
FROM repository.int.compax.at:5001/rp-spring-boot:${AAX_VERSION}

USER compax

COPY ./target/*.jar /fwc-logistic-subsystem.jar

EXPOSE 8080
ENTRYPOINT ["/vol1/entrypoint.sh", "/fwc-logistic-subsystem.jar"]
