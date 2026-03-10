FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/bank-backend-0.0.1-SNAPSHOT.jar bank-backend.jar
CMD ["java", "-jar", "bank-backend.jar"]