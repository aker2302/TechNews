FROM openjdk:17-jdk

LABEL authors="Aker"

WORKDIR /app

COPY target/TechNews-0.0.1-SNAPSHOT.jar /app/TechNews-0.0.1-SNAPSHOT.jar

EXPOSE 8090

CMD ["java","-jar","TechNews-0.0.1-SNAPSHOT.jar"]
