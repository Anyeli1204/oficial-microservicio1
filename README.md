# 🚀 ScrapeTok Accounts Service

Microservicio de gestión de cuentas y perfiles para la plataforma ScrapeTok, desarrollado con Spring Boot, PostgreSQL y Docker.

## 📋 Características

- ✅ **Registro de usuarios** (normales y administradores)
- ✅ **Autenticación JWT** segura
- ✅ **Gestión de perfiles** (actualización parcial y completa)
- ✅ **Cambio de contraseñas** (solo propias)
- ✅ **Gestión de usuarios** (activar/desactivar, promoción a admin)
- ✅ **Envío de emails** de bienvenida
- ✅ **Base de datos PostgreSQL** con migraciones
- ✅ **Documentación OpenAPI/Swagger**

## 🛠️ Tecnologías

- **Backend**: Spring Boot 3.2.0, Java 17
- **Base de datos**: PostgreSQL 15
- **Autenticación**: JWT (JSON Web Tokens)
- **Email**: Spring Mail con Gmail SMTP
- **Contenedores**: Docker & Docker Compose
- **Documentación**: OpenAPI 3 / Swagger UI

## 🚀 Despliegue Rápido

### Prerequisitos
- Docker y Docker Compose instalados
- Git

### 1. Clonar el repositorio
```bash
git clone <tu-repositorio-github>
cd microservicio1
```

### 2. Configurar variables de entorno
```bash
cp env.example .env
# Editar .env con tus valores reales
```

### 3. Ejecutar con Docker Compose
```bash
docker-compose -f docker-compose.production.yml up -d
```

### 4. Verificar funcionamiento
```bash
# Health check
curl http://localhost:8081/api/v1/actuator/health

# Crear usuario
curl -X POST http://localhost:8081/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstname": "Test",
    "lastname": "User",
    "username": "testuser"
  }'
```

## 🌐 Endpoints Disponibles

### Públicos (Sin autenticación)
- `POST /api/v1/auth/signup` - Registrar usuario
- `POST /api/v1/auth/signupadmin` - Registrar administrador
- `POST /api/v1/auth/signin` - Iniciar sesión
- `GET /api/v1/actuator/health` - Health check

### Autenticados (Requieren JWT)
- `GET /api/v1/auth/users` - Listar usuarios (Solo ADMIN)
- `GET /api/v1/auth/profile/{id}` - Obtener perfil
- `PUT /api/v1/auth/profile/{id}` - Actualizar perfil
- `PATCH /api/v1/auth/change-password/{id}` - Cambiar contraseña
- `PATCH /api/v1/auth/upgrade-to-admin` - Promover a admin (Solo ADMIN)
- `PATCH /api/v1/auth/deactivate-user` - Desactivar usuario (Solo ADMIN)
- `PATCH /api/v1/auth/activate-user/{id}` - Activar usuario (Solo ADMIN)

## 📚 Documentación API

Una vez ejecutado el servicio, puedes acceder a la documentación Swagger en:
- **Swagger UI**: http://localhost:8081/api/v1/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api/v1/v3/api-docs

## 🔧 Configuración

### Variables de Entorno (.env)
```bash
# Base de datos
DB_PASSWORD=tu_password_seguro

# JWT
JWT_SECRET=tu_clave_secreta_muy_larga_de_al_menos_32_caracteres

# Email
MAIL_USERNAME=tu-email@gmail.com
MAIL_PASSWORD=tu_app_password

# CORS
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

## 🗄️ Base de Datos

- **Puerto**: 5432
- **Base de datos**: scrapetok_accounts
- **Usuario**: postgres
- **Migraciones**: Automáticas con Flyway

## 🔍 Monitoreo

- **Health Check**: `GET /api/v1/actuator/health`
- **Logs**: `docker-compose -f docker-compose.production.yml logs -f`

## 🛠️ Comandos Útiles

```bash
# Ver estado
docker-compose -f docker-compose.production.yml ps

# Ver logs
docker-compose -f docker-compose.production.yml logs -f accounts-service

# Reiniciar
docker-compose -f docker-compose.production.yml restart accounts-service

# Detener
docker-compose -f docker-compose.production.yml down

# Detener y eliminar volúmenes
docker-compose -f docker-compose.production.yml down -v
```

## 📝 Estructura del Proyecto

```
microservicio1/
├── src/main/java/com/scrapetok/accounts/
│   ├── controller/          # Controladores REST
│   ├── service/            # Lógica de negocio
│   ├── domain/             # Entidades y DTOs
│   ├── repository/         # Repositorios JPA
│   ├── security/           # Configuración JWT
│   ├── config/             # Configuraciones Spring
│   └── exception/          # Manejo de excepciones
├── src/main/resources/
│   ├── application*.yml    # Configuraciones por perfil
│   └── db/migration/       # Scripts de migración
├── docker-compose.production.yml
├── Dockerfile
├── env.example
└── README.md
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 Autor

**Anyeli Tamara** - [@anyeli1234](https://github.com/anyeli1234)

---

⭐ ¡No olvides dar una estrella al proyecto si te fue útil!