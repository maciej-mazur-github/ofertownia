FROM maven:3.9.9-amazoncorretto-17-al2023 AS MAVEN_BUILD
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package

FROM amazoncorretto:latest
EXPOSE 8080
COPY --from=MAVEN_BUILD /target/ofertownia-*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]