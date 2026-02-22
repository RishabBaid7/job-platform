# Use lightweight JDK
# It pulls a lightweight Alpine Linux image that has JDK 17 pre-installed. 
FROM eclipse-temurin:17-jdk-jammy 

# Set working directory
# Sets the working directory inside the container to /app
WORKDIR /app

# Copy jar from target
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8085

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]