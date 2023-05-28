# Use a base image with OpenJDK 19
FROM openjdk:19-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file of your Spring app into the container
COPY target/tmp-0.0.1-SNAPSHOT.jar /app/tmp.jar

# Expose the port that your Spring app will listen on
EXPOSE 8082

# Define the command to run your Spring app
CMD ["java", "-jar", "tmp.jar"]
