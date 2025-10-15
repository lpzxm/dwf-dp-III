-- Script para crear la base de datos y la tabla de usuarios usada por el proyecto
CREATE DATABASE IF NOT EXISTS bdduser CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bdduser;

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL UNIQUE,
  `password` VARCHAR(255),
  `firstname` VARCHAR(255),
  `lastname` VARCHAR(255),
  `age` INT,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Usuario de prueba (password: password123)
-- Generar el hash bcrypt para 'password123' antes de insertar si se desea,
-- por ejemplo con la dependencia de Spring Security o con una herramienta.
-- Aquí se inserta sin hash sólo como ejemplo (no recomendado en producción).
INSERT INTO `user` (username, password, firstname, lastname, age) VALUES
('adrian', '$2a$10$7qvQp8xQ9w1Hq1Jc1QYj9ORvZbW9I9hK0qWc1kYpQK1Q1yZgY1a', 'Adrian', 'Lopez', 30);
