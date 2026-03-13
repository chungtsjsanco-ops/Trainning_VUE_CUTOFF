# Build stage
FROM maven:3.6.2-jdk-8 AS maven_build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Run stage
FROM bellsoft/liberica-openjre-debian:8u442
RUN addgroup --gid 2000 --system app && \
    adduser --uid 2000 --system --ingroup app app
USER app
EXPOSE 8080
COPY --from=maven_build /app/target/*.jar /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
