FROM openjdk:11 AS build

COPY pom.xml mvnw ./
RUN chmod +x mvnw
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package


FROM openjdk:11
WORKDIR blog-api
COPY --from=build target/*.jar blog-api.jar
ENTRYPOINT ["java", "-jar", "blog-api.jar"]