FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD cash-agent.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]