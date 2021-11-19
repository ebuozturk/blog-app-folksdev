FROM openjdk:11 AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw clean package -DskipTests

FROM openjdk:11
WORKDIR blog-api
COPY --from=build target/*.jar blog-api.jar
ENTRYPOINT ["java", "-jar", "blog-api.jar"]