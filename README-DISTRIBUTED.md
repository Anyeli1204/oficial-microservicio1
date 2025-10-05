# 🏗️ Despliegue Distribuido - 2 Máquinas Virtuales

## 📋 Arquitectura

- **MV1 (Base de datos):** PostgreSQL + Adminer
- **MV2 (Microservicio):** Spring Boot API

## 🚀 Pasos de Despliegue

### 1️⃣ **MV1 - Base de Datos**

```bash
# Clonar repositorio
git clone https://github.com/Anyeli1204/oficial-microservicio1.git
cd oficial-microservicio1

# Levantar base de datos
docker-compose -f docker-compose.bd.yml up -d

# Verificar que esté corriendo
docker ps

# Anotar la IP privada de esta MV
ip addr show
```

### 2️⃣ **MV2 - Microservicio**

```bash
# Crear carpeta para el microservicio
mkdir microservicio1
cd microservicio1

# Crear archivo .env
cp ../env.distributed.yml .env

# Editar .env con la IP privada de MV1
nano .env
# Cambiar: DB_HOST=IP_PRIVADA_DE_MV1

# Ejecutar microservicio
docker run --env-file /home/ubuntu/microservicio1/.env \
  -p 8081:8081 \
  --name scrapetok_accounts_c \
  anyeli1234/scrapetok-accounts-service:latest
```

## 🌐 URLs de Acceso

### **MV1 (Base de datos):**
- **Adminer:** `http://IP_PUBLICA_MV1:8080`

### **MV2 (Microservicio):**
- **API:** `http://IP_PUBLICA_MV2:8081/api/v1`
- **Swagger:** `http://IP_PUBLICA_MV2:8081/api/v1/swagger-ui/index.html`

## 🔧 Configuración de Seguridad

### **Security Groups MV1:**
- Puerto 5432: Solo desde MV2
- Puerto 8080: Desde cualquier lugar (Adminer)

### **Security Groups MV2:**
- Puerto 8081: Desde cualquier lugar (API)

## 📝 Variables de Entorno

### **MV1 (docker-compose.bd.yml):**
- `POSTGRES_PASSWORD=utec`
- `POSTGRES_DB=scrapetok_accounts`

### **MV2 (.env):**
- `DB_HOST=IP_PRIVADA_MV1`
- `DB_PASSWORD=utec`
- `JWT_SECRET=tu_secret_largo`
- `ALLOWED_ORIGINS=con_tu_ip_publica`

## ✅ Verificación

```bash
# En MV1
curl http://localhost:8080

# En MV2
curl http://localhost:8081/api/v1/actuator/health
```
