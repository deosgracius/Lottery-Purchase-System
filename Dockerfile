# ---- build stage: compile + package the fat JAR ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -B -DskipTests package

# ---- run stage: slim JRE ----
FROM eclipse-temurin:21-jre
WORKDIR /app
# Data dir is a mounted volume in production so JSON data survives redeploys.
ENV DATA_DIR=/data
ENV PORT=8080
RUN mkdir -p /data
COPY --from=build /app/target/lottery-purchase-system.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
