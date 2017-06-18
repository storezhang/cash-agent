FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD cash-agent-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]