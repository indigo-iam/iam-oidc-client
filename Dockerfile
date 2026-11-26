FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE=target/iam-test-client-*.jar
COPY ${JAR_FILE} /app/iam-oidc-client.jar

RUN useradd --system --uid 10001 --home-dir /app --shell /usr/sbin/nologin appuser \
    && chown -R appuser:appuser /app
USER 10001:10001

EXPOSE 9090
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/iam-oidc-client.jar"]
