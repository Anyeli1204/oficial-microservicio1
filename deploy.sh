#!/bin/bash

echo "🚀 Desplegando Microservicio 1: Servicio de Cuentas y Perfiles"
echo "============================================================="

# Verificar que Docker esté instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Instalando Docker..."
    curl -fsSL https://get.docker.com -o get-docker.sh
    sh get-docker.sh
    sudo usermod -aG docker $USER
    echo "✅ Docker instalado. Reinicia la sesión y ejecuta el script nuevamente."
    exit 1
fi

# Verificar que Docker Compose esté instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado. Instalando..."
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi

# Crear archivo .env si no existe
if [ ! -f .env.prod ]; then
    echo "📝 Creando archivo .env.prod..."
    cat > .env.prod << EOF
# Variables de entorno para producción
DB_HOST=scrapetok-accounts-db.cluster-cb6q2ukcu3hs.us-east-1.rds.amazonaws.com
DB_PORT=5432
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=>VFOUBP79E*a)JtGE50R8gY7!cQN
JWT_SECRET=mi-clave-jwt-super-segura-para-scrapetok-2024
JWT_EXPIRATION=86400000
EOF
    echo "⚠️  Archivo .env.prod creado. Verifica las variables antes de continuar."
fi

# Compilar el proyecto
echo "🔨 Compilando proyecto..."
mvn clean package -DskipTests

# Construir imagen Docker
echo "🐳 Construyendo imagen Docker..."
docker build -f Dockerfile.prod -t accounts-service:latest .

# Detener contenedores existentes
echo "🛑 Deteniendo contenedores existentes..."
docker-compose -f docker-compose.prod.yml down

# Iniciar servicios
echo "🚀 Iniciando servicios..."
docker-compose -f docker-compose.prod.yml up -d

# Verificar estado
echo "📊 Verificando estado de los servicios..."
sleep 10
docker-compose -f docker-compose.prod.yml ps

# Verificar health check
echo "🏥 Verificando health check..."
sleep 5
curl -f http://localhost:8081/api/v1/actuator/health || echo "❌ Health check falló"

echo ""
echo "✅ Despliegue completado!"
echo "🌐 Microservicio disponible en: http://localhost:8081"
echo "📊 Health check: http://localhost:8081/api/v1/actuator/health"
echo "📋 Para ver logs: docker-compose -f docker-compose.prod.yml logs -f"
echo "🛑 Para detener: docker-compose -f docker-compose.prod.yml down"
