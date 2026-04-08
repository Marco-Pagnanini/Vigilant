FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew

COPY build.gradle settings.gradle .
RUN ./gradlew dependencies --no-daemon 2>/dev/null || true

COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
