# Etapa de build
FROM gradle:8.4.0-jdk17 AS build
COPY . /app
WORKDIR /app
RUN gradle clean installDist

# Etapa final
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/build/install/servidorFlashcard /app
EXPOSE 8080
CMD ["/app/bin/servidorFlashcard"]
