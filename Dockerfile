FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD ./target/cash-agent.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java", "$JAVA_OPTS", "-Djava.security.egd=file:/dev/./urandom",  "-Dspring.config.location=file:/etc/cash-agent/config/application.yml", "-jar", "/app.jar"]
