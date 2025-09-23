#!/bin/bash

# Cargar variables de entorno
export DB_HOST="scrapetok-accounts-db.cluster-cb6q2ukcu3hs.us-east-1.rds.amazonaws.com"
export DB_PORT="5432"
export DB_NAME="postgres"
export DB_USER="postgres"
export DB_PASSWORD="tu_contraseña_aqui"
export JWT_SECRET="tu-clave-jwt-super-segura-de-al-menos-256-bits"
export JWT_EXPIRATION="86400000"
export SPRING_PROFILES_ACTIVE="prod"

echo "Variables de entorno cargadas:"
echo "DB_HOST: $DB_HOST"
echo "DB_PORT: $DB_PORT"
echo "DB_NAME: $DB_NAME"
echo "DB_USER: $DB_USER"
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"

# Ejecutar el microservicio
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
