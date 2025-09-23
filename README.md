# Microservicio 1: Servicio de Cuentas y Perfiles

Este microservicio maneja la gestión de usuarios y administradores del sistema ScrapeTok, incluyendo autenticación, autorización y gestión de perfiles.

## 🚀 Características

- **Registro de usuarios** y administradores
- **Autenticación JWT** con Spring Security
- **Gestión de perfiles** de usuarios y administradores
- **Base de datos PostgreSQL** con migraciones
- **Envío de emails** de bienvenida
- **Docker** y Docker Compose para deployment
- **Manejo de excepciones** global

## 🏗️ Arquitectura

```
src/main/java/com/scrapetok/accounts/
├── AccountsServiceApplication.java    # Clase principal
├── config/                           # Configuraciones
│   ├── ModelMapperConfig.java
│   └── SecurityConfig.java
├── controller/                       # Controladores REST
│   └── AuthController.java
├── domain/                          # Entidades y DTOs
│   ├── User.java
│   ├── AdminProfile.java
│   ├── enums/Role.java
│   └── dto/
├── exception/                       # Manejo de excepciones
│   ├── GlobalExceptionHandler.java
│   ├── EmailAlreadyInUseException.java
│   └── ResourceNotFoundException.java
├── repository/                      # Repositorios JPA
│   ├── UserRepository.java
│   └── AdminProfileRepository.java
├── security/                        # Configuración de seguridad
│   ├── JwtUtil.java
│   ├── MyUserDetailsService.java
│   └── JwtRequestFilter.java
└── service/                         # Lógica de negocio
    ├── AuthService.java
    └── email/EmailService.java
```

## 🗄️ Base de Datos

### Tablas principales:
- **users**: Información básica de usuarios
- **admin_profiles**: Perfiles extendidos para administradores

### Esquema de base de datos:
```sql
-- Tabla de usuarios
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    creation_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de perfiles de administrador
CREATE TABLE admin_profiles (
    id BIGINT PRIMARY KEY,
    admision_to_admin_date DATE NOT NULL,
    admision_to_admin_time TIME NOT NULL,
    total_questions_answered INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);
```

## 🔌 API Endpoints

### Autenticación
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/api/v1/auth/signup` | Registro de usuario | No |
| POST | `/api/v1/auth/signupadmin` | Registro de administrador | No |
| POST | `/api/v1/auth/signin` | Login | No |

### Gestión de Usuarios
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/v1/auth/users` | Obtener todos los usuarios | ADMIN |
| GET | `/api/v1/auth/profile/{userId}` | Obtener perfil de usuario | USER/ADMIN |

### Health Check
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/v1/actuator/health` | Estado del servicio | No |

## 📝 Ejemplos de Uso

### Registro de Usuario
```bash
curl -X POST http://localhost:8081/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@ejemplo.com",
    "password": "password123",
    "firstname": "Juan",
    "lastname": "Pérez",
    "username": "juanperez"
  }'
```

### Login
```bash
curl -X POST http://localhost:8081/api/v1/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@ejemplo.com",
    "password": "password123"
  }'
```

### Obtener Perfil (con JWT)
```bash
curl -X GET http://localhost:8081/api/v1/auth/profile/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🐳 Docker

### Construcción y ejecución con Docker Compose:
```bash
# Construir y ejecutar todos los servicios
docker-compose up --build

# Ejecutar solo el microservicio (requiere DB externa)
docker-compose up accounts-service

# Ejecutar con pgAdmin incluido
docker-compose --profile admin up
```

### Variables de entorno:
```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar variables según tu entorno
nano .env
```

## 🔧 Configuración Local

### Prerrequisitos:
- Java 17+
- Maven 3.6+
- PostgreSQL 13+

### Pasos:
1. **Clonar y configurar:**
```bash
cd microservicio1-accounts
cp .env.example .env
# Editar .env con tus configuraciones
```

2. **Configurar base de datos:**
```bash
# Crear base de datos PostgreSQL
createdb scrapetok_accounts
```

3. **Ejecutar migraciones:**
```bash
# Las migraciones se ejecutan automáticamente al iniciar
mvn spring-boot:run
```

4. **Verificar funcionamiento:**
```bash
curl http://localhost:8081/api/v1/actuator/health
```

## 🔐 Seguridad

- **JWT**: Tokens con expiración configurable
- **CORS**: Configurado para orígenes específicos
- **Validación**: Validación de entrada en DTOs
- **Encriptación**: Contraseñas encriptadas con BCrypt
- **Roles**: Sistema de roles USER/ADMIN

## 📊 Monitoreo

- **Health Check**: `/api/v1/actuator/health`
- **Métricas**: `/api/v1/actuator/metrics`
- **Info**: `/api/v1/actuator/info`

## 🧪 Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

## 🚀 Deployment

### Variables de entorno para producción:
```bash
SPRING_PROFILES_ACTIVE=prod
DB_HOST=your-db-host
DB_PASSWORD=secure-password
JWT_SECRET=very-secure-secret
MAIL_USERNAME=your-email
MAIL_PASSWORD=your-app-password
```

### Railway/Heroku:
1. Conectar repositorio
2. Configurar variables de entorno
3. Deploy automático

## 🤝 Contribución

1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a branch (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👥 Equipo

- **ANYELI** - Desarrollo y automatización de deployment
- **ScrapeTok Team** - Arquitectura y revisión

## 📞 Soporte

Para soporte, contacta a: support@scrapetok.com
