FROM openjdk:17
FROM postgres
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sudo","-u postgres","psql"]
ENTRYPOINT ["CREATE","DATABASE","elamigos"]
ENTRYPOINT ["exit"]
ENTRYPOINT ["java","-jar","/app.jar"]
#FROM debian
#RUN