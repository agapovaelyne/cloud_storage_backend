FROM adoptopenjdk/openjdk11
EXPOSE 8081
COPY target/CloudKeeper-0.0.1-SNAPSHOT.jar cloudKeeper.jar
ENTRYPOINT ["java", "-jar", "/cloudKeeper.jar"]