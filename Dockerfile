FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} iam-oidc-client.jar

EXPOSE 9090

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/iam-oidc-client.jar"]