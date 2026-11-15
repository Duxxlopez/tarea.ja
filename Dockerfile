# --- ETAPA 1: Construcción (Build con Gradle global) ---
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copiar archivos de configuración y código fuente
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# Compilar usando gradle directamente
RUN gradle bootJar --no-daemon

# --- ETAPA 2: Ejecución (Runtime ligero) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR compilado desde la primera etapa
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto interno de Spring Boot
EXPOSE 8080

# Comando de inicio del contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]