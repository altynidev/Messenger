# 1. Build Stage (Using Java 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Run Stage (Using Java 21 Runtime)
FROM eclipse-temurin:21-jre
WORKDIR /app
# Note: copying 'messenger-0.0.1-SNAPSHOT.jar' because artifactId=messenger in pom.xml
COPY --from=build /app/target/messenger-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]