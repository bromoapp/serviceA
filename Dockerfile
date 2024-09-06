FROM openjdk:17-alpine
WORKDIR /apt
ARG JAR_FILE=target/service-a.jar 
COPY ${JAR_FILE} /apt/service-a.jar
EXPOSE 8081

ENTRYPOINT ["java","-jar","/apt/service-a.jar"]