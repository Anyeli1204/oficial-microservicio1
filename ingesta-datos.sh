#!/bin/bash

echo "📊 Iniciando ingesta de datos (20,000 registros)"
echo "================================================"

# URL del microservicio
MICROSERVICE_URL="http://localhost:8081/api/v1"

# Función para crear usuario
create_user() {
    local id=$1
    local email="usuario${id}@test.com"
    local firstname="Usuario${id}"
    local lastname="Test${id}"
    local username="usuario${id}"
    
    curl -X POST "${MICROSERVICE_URL}/auth/signup" \
        -H "Content-Type: application/json" \
        -d "{
            \"email\": \"${email}\",
            \"password\": \"password123\",
            \"firstname\": \"${firstname}\",
            \"lastname\": \"${lastname}\",
            \"username\": \"${username}\"
        }" > /dev/null 2>&1
}

# Crear usuarios
echo "👥 Creando 20,000 usuarios..."
for i in $(seq 1 20000); do
    create_user $i
    
    # Mostrar progreso cada 1000 usuarios
    if [ $((i % 1000)) -eq 0 ]; then
        echo "✅ Creados $i usuarios..."
    fi
done

echo "🎉 Ingesta completada: 20,000 usuarios creados"
echo "📊 Verificar en: ${MICROSERVICE_URL}/auth/users"
