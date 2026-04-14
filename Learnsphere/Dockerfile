# Step 1: Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

# 👉 MOVE INTO YOUR ACTUAL PROJECT FOLDER
WORKDIR /app/Learnsphere

RUN mvn clean package -DskipTests


# Step 2: Run stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/Learnsphere/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
