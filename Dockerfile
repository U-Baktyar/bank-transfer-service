FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/bank-backend-0.0.1-SNAPSHOT.jar bank-backend.jar
CMD ["java", "-jar", "bank-backend.jar"]