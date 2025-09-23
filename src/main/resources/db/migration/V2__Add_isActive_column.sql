-- Agregar columna isActive a la tabla users
ALTER TABLE users ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT true;
