#!/bin/bash

# Script de inicio para el Microservicio 1: Servicio de Cuentas y Perfiles

echo "🚀 Iniciando Microservicio 1: Servicio de Cuentas y Perfiles"
echo "============================================================"

# Verificar si Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker no está corriendo. Por favor, inicia Docker Desktop."
    exit 1
fi

# Verificar si el archivo .env existe
if [ ! -f .env ]; then
    echo "📝 Creando archivo .env desde .env.example..."
    cp .env.example .env
    echo "⚠️  Por favor, edita el archivo .env con tus configuraciones antes de continuar."
    echo "   Especialmente las credenciales de email y base de datos."
    read -p "¿Continuar con la configuración por defecto? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Función para mostrar menú
show_menu() {
    echo ""
    echo "¿Qué deseas hacer?"
    echo "1) 🐳 Iniciar con Docker Compose (recomendado)"
    echo "2) 🏗️  Construir y ejecutar localmente"
    echo "3) ☁️  Ejecutar con Aurora PostgreSQL (AWS)"
    echo "4) 🧪 Ejecutar tests"
    echo "5) 🗄️  Solo iniciar base de datos"
    echo "6) 🧹 Limpiar contenedores"
    echo "7) ❌ Salir"
    echo ""
}

# Función para iniciar con Docker Compose
start_docker() {
    echo "🐳 Iniciando servicios con Docker Compose..."
    docker-compose down
    docker-compose up --build -d
    
    echo ""
    echo "✅ Servicios iniciados:"
    echo "   - Microservicio: http://localhost:8081"
    echo "   - PostgreSQL: localhost:5432"
    echo "   - pgAdmin: http://localhost:5050 (usar perfil admin)"
    echo ""
    echo "📊 Para ver logs: docker-compose logs -f"
    echo "🛑 Para detener: docker-compose down"
}

# Función para construir y ejecutar localmente
start_local() {
    echo "🏗️  Construyendo y ejecutando localmente..."
    
    # Verificar si Maven está instalado
    if ! command -v mvn &> /dev/null; then
        echo "❌ Maven no está instalado. Por favor, instala Maven."
        exit 1
    fi
    
    # Verificar si PostgreSQL está corriendo
    if ! pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
        echo "⚠️  PostgreSQL no está corriendo en localhost:5432"
        echo "   Iniciando solo la base de datos con Docker..."
        docker-compose up postgres -d
        sleep 5
    fi
    
    echo "🔨 Compilando proyecto..."
    mvn clean package -DskipTests
    
    echo "🚀 Ejecutando aplicación..."
    mvn spring-boot:run
}

# Función para ejecutar con Aurora PostgreSQL
start_aws() {
    echo "☁️  Ejecutando con Aurora PostgreSQL (AWS)..."
    
    # Verificar si Maven está instalado
    if ! command -v mvn &> /dev/null; then
        echo "❌ Maven no está instalado. Por favor, instala Maven."
        exit 1
    fi
    
    # Verificar si existe el archivo .env.prod
    if [ ! -f .env.prod ]; then
        echo "❌ Archivo .env.prod no encontrado."
        echo "   Por favor, crea el archivo .env.prod con las variables de Aurora PostgreSQL."
        echo ""
        echo "   Ejemplo de contenido:"
        echo "   DB_HOST=scrapetok-accounts-db.cluster-xxxxx.us-east-1.rds.amazonaws.com"
        echo "   DB_PORT=5432"
        echo "   DB_NAME=postgres"
        echo "   DB_USER=postgres"
        echo "   DB_PASSWORD=>VFOUBP79E*a)JtGE50R8gY7!cQN"
        echo "   JWT_SECRET=tu-clave-jwt-super-segura"
        echo "   JWT_EXPIRATION=86400000"
        echo "   SPRING_PROFILES_ACTIVE=prod"
        exit 1
    fi
    
    echo "📋 Cargando variables de entorno desde .env.prod..."
    
    # Cargar variables de entorno
    export DB_HOST="scrapetok-accounts-db.cluster-cb6q2ukcu3hs.us-east-1.rds.amazonaws.com"
    export DB_PORT="5432"
    export DB_NAME="postgres"
    export DB_USER="postgres"
    export DB_PASSWORD=">VFOUBP79E*a)JtGE50R8gY7!cQN"
    export JWT_SECRET="mi-clave-jwt-super-segura-para-scrapetok-2024"
    export JWT_EXPIRATION="86400000"
    export SPRING_PROFILES_ACTIVE="prod"
    
    echo "✅ Variables cargadas:"
    echo "   DB_HOST: $DB_HOST"
    echo "   DB_PORT: $DB_PORT"
    echo "   DB_NAME: $DB_NAME"
    echo "   DB_USER: $DB_USER"
    echo "   SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
    
    echo ""
    echo "⚠️  IMPORTANTE: Asegúrate de que:"
    echo "   1. Tu Aurora PostgreSQL esté corriendo"
    echo "   2. El Security Group permita conexiones en puerto 5432"
    echo "   3. La contraseña en el script sea correcta"
    echo ""
    read -p "¿Continuar? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "❌ Operación cancelada."
        exit 1
    fi
    
    echo "🔨 Compilando proyecto..."
    mvn clean package -DskipTests
    
    echo "🚀 Ejecutando aplicación con Aurora PostgreSQL..."
    mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
}

# Función para ejecutar tests
run_tests() {
    echo "🧪 Ejecutando tests..."
    mvn test
}

# Función para iniciar solo la base de datos
start_db_only() {
    echo "🗄️  Iniciando solo la base de datos..."
    docker-compose up postgres -d
    
    echo ""
    echo "✅ Base de datos iniciada en localhost:5432"
    echo "   - Usuario: postgres"
    echo "   - Contraseña: password"
    echo "   - Base de datos: scrapetok_accounts"
}

# Función para limpiar contenedores
cleanup() {
    echo "🧹 Limpiando contenedores..."
    docker-compose down -v
    docker system prune -f
    echo "✅ Limpieza completada"
}

# Función para verificar estado
check_status() {
    echo ""
    echo "📊 Estado de los servicios:"
    echo "=========================="
    
    # Verificar microservicio
    if curl -s http://localhost:8081/api/v1/actuator/health > /dev/null 2>&1; then
        echo "✅ Microservicio: http://localhost:8081 (ACTIVO)"
    else
        echo "❌ Microservicio: http://localhost:8081 (INACTIVO)"
    fi
    
    # Verificar base de datos
    if pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
        echo "✅ PostgreSQL: localhost:5432 (ACTIVO)"
    else
        echo "❌ PostgreSQL: localhost:5432 (INACTIVO)"
    fi
    
    # Verificar pgAdmin
    if curl -s http://localhost:5050 > /dev/null 2>&1; then
        echo "✅ pgAdmin: http://localhost:5050 (ACTIVO)"
    else
        echo "❌ pgAdmin: http://localhost:5050 (INACTIVO)"
    fi
}

# Loop principal
while true; do
    show_menu
    read -p "Selecciona una opción (1-7): " choice
    
    case $choice in
        1)
            start_docker
            check_status
            ;;
        2)
            start_local
            ;;
        3)
            start_aws
            ;;
        4)
            run_tests
            ;;
        5)
            start_db_only
            check_status
            ;;
        6)
            cleanup
            ;;
        7)
            echo "👋 ¡Hasta luego!"
            exit 0
            ;;
        *)
            echo "❌ Opción inválida. Por favor, selecciona 1-7."
            ;;
    esac
    
    echo ""
    read -p "Presiona Enter para continuar..."
done
