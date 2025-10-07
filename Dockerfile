FROM gradle:8.9-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon


FROM eclipse-temurin:21-jre
WORKDIR /app
ENV API_KEY=dev-key \
DOWNSTREAM_KEY=downstream-key \
VIDEO_BASE_URL=http://video:8001 \
HIGHLIGHT_BASE_URL=http://highlight:8002
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]