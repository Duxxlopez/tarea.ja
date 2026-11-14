# Usar una imagen oficial y actualizada de OpenJDK (Eclipse Temurin)
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo jar generado por Gradle al contenedor
COPY build/libs/*.jar app.jar

# Exponer el puerto 8081
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]