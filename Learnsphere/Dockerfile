# Step 1: Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy everything
COPY . .

# Build jar
RUN mvn clean package -DskipTests


# Step 2: Run stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]