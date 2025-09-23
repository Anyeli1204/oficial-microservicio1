# Usar imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Información del mantenedor
LABEL maintainer="ScrapeTok Team"
LABEL service="accounts-service"

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 8081

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=scrapetok_accounts
ENV DB_USER=postgres
ENV DB_PASSWORD=password
ENV JWT_SECRET=mySecretKey
ENV JWT_EXPIRATION=86400000

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/accounts-service-1.0.0.jar"]
