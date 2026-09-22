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