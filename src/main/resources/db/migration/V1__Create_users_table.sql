-- Tabla principal de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    creation_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimizar consultas
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_users_creation_date ON users(creation_date);

-- Tabla de perfiles de administrador
CREATE TABLE IF NOT EXISTS admin_profiles (
    id BIGINT PRIMARY KEY,
    admision_to_admin_date DATE NOT NULL,
    admision_to_admin_time TIME NOT NULL,
    total_questions_answered INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Índices para admin_profiles
CREATE INDEX IF NOT EXISTS idx_admin_profiles_active ON admin_profiles(is_active);
CREATE INDEX IF NOT EXISTS idx_admin_profiles_admission_date ON admin_profiles(admision_to_admin_date);

-- Función para actualizar updated_at automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para actualizar updated_at
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_admin_profiles_updated_at 
    BEFORE UPDATE ON admin_profiles 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insertar usuario admin por defecto (opcional)
-- INSERT INTO users (email, password, firstname, lastname, username, role, creation_date) 
-- VALUES ('admin@scrapetok.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVh3Zk1jQ7wOa8vz6p9K2vF4HO', 'Admin', 'System', 'admin', 'ADMIN', CURRENT_DATE);

-- INSERT INTO admin_profiles (id, admision_to_admin_date, admision_to_admin_time, total_questions_answered, is_active)
-- VALUES ((SELECT id FROM users WHERE email = 'admin@scrapetok.com'), CURRENT_DATE, CURRENT_TIME, 0, true);
