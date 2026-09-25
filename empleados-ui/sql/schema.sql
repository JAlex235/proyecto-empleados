## Se genera la base de datos para el proyecto
CREATE DATABASE IF NOT EXISTS empleados_db;
USE empleados_db;

## Se genera la tabla empleados
CREATE TABLE empleados (
    ## Llave primaria de empleados autogenerada de tipo int
    id INT AUTO_INCREMENT PRIMARY KEY,
    ## Nombre completo no nulo
    nombre_completo VARCHAR(100) NOT NULL,
    ## Departamento del empleado no nulo
    departamento VARCHAR(100) NOT NULL,
    ## Salario mensual de 2 decimales
    salario_mensual DECIMAL(10,2) NOT NULL,
    ## Fecha de ingreso de tipo fecha
    fecha_contratacion DATE NOT NULL,
    ## Empleado activo de tipo booleano
    activo BOOLEAN NOT NULL DEFAULT TRUE
);


## Datos iniciales para probar
INSERT INTO empleados
    (nombre_completo, departamento, salario_mensual,
    fecha_contratacion, activo, anios_experiencia)
VALUES
    ('Ana López', 'Recursos Humanos', 6500.00, '2023-02-15', TRUE, 3),
    ('Carlos Méndez', 'Arquitectura', 10500.00, '2017-06-10', TRUE, 12),
    ('María González', 'Contabilidad', 7200.50, '2021-09-01', TRUE, 7),
    ('Luis Ramírez', 'Soporte Técnico', 5800.00, '2025-01-20', FALSE, 1),
    ('Sofía Castillo', 'Desarrollo', 12000.00, '2014-03-05', TRUE, 16);

## Se agrega la columna anios_experiencia a la tabla empleados para examen parcial 2
ALTER TABLE empleados
ADD COLUMN anios_experiencia INT NOT NULL DEFAULT 0;