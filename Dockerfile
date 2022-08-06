#FROM openjdk:17
#FROM postgres
FROM debian
FROM postgres
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN apt-get update -y
RUN apt-get install sudo -y
RUN apt-get install postgresql -y
RUN sudo -u postgres psql
RUN ["CREATE","DATABASE","elamigos"]
RUN ["exit"]
RUN ["java","-jar","/app.jar"]
