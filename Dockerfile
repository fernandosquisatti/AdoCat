# AdoCat — build Java 17 + Maven, sem Nixpacks
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Compila a aplicacao Spring Boot com as dependencias do pom.xml.
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# Imagem final menor: somente Java para executar a aplicacao.
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "/app/app.jar"]
