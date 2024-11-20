FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/gatewayserver-0.0.5.jar
COPY ${JAR_FILE} gatewayserver.jar
EXPOSE 8066
ENTRYPOINT ["java", "-jar", "gatewayserver.jar"]