# Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

# The application's .jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's .jar to the container
ADD ${JAR_FILE} app.jar
COPY ./src/main/java/com/rsww/transport_service/transport/scrapTransport.csv /app/

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]