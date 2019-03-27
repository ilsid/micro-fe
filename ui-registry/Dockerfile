FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} ui-registry.jar
ENTRYPOINT ["java", "-jar", "/ui-registry.jar"]

