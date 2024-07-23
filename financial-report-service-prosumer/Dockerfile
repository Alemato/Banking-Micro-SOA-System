# Stage 1: Build
FROM maven:3.9.7-amazoncorretto-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY financial-report-service-prosumer/pom.xml .

# Copy the WSDL and OpenAPI files into the build directory
COPY wsdl /app/../wsdl
COPY openapi /app/../openapi

# Copy the source code into the working directory
COPY financial-report-service-prosumer/src /app/src

# Run code generation and Maven build command, skipping tests
RUN mvn clean generate-sources package -DskipTests

# Stage 2: Run
FROM amazoncorretto:17.0.12-alpine3.19

# Set the working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 9086
EXPOSE 9086

# Add labels to the image
LABEL name="Banking Micro-SOA System - Financial Report Service Prosumer" \
      description="Financial Report Service Prosumer for Banking Micro-SOA System" \
      version="0.0.1" \
      authors="University of L'Aquila"

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]