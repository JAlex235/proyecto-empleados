# Proyecto Empleados

Aplicación de escritorio desarrollada en Java para la gestión de empleados de una empresa.  
El proyecto implementa un CRUD completo utilizando **Swing**, **Maven multi-módulo**, **JDBC** y **MariaDB**.

## Funcionalidades

La aplicación permite:

- Registrar empleados.
- Listar todos los empleados.
- Seleccionar y editar un empleado existente.
- Cambiar el estado de un empleado entre activo e inactivo.
- Eliminar físicamente un empleado, con confirmación previa.
- Validar los datos antes de enviarlos a la base de datos.
- Mostrar errores mediante ventanas de diálogo sin cerrar la aplicación.

## Datos de un empleado

Cada empleado contiene:

- ID autogenerado.
- Nombre completo.
- Departamento.
- Salario mensual.
- Fecha de contratación.
- Estado activo o inactivo.

## Reglas de validación

Antes de realizar una operación de creación o actualización se verifica que:

- El nombre no esté vacío.
- El departamento no esté vacío.
- El salario sea un valor numérico mayor que cero.
- Se haya seleccionado una fecha de contratación.
- La fecha de contratación no sea futura.

## Arquitectura del proyecto

El proyecto utiliza Maven multi-módulo para separar responsabilidades.

```text
proyecto-empleados/
├── pom.xml
├── empleados-core/
│   ├── pom.xml
│   └── src/main/java/edu/umg/programacion2/proyecto/
│       ├── modelo/
│       │   └── Empleado.java
│       └── dao/
│           └── EmpleadoDAO.java
│
├── empleados-ui/
│   ├── pom.xml
│   ├── sql/
│   │   └── schema.sql
│   └── src/main/java/edu/umg/programacion2/proyecto/
│       ├── MainUI.java
│       └── ui/
│           └── VentanaPrincipal.java
│
└── variantes/
    └── variante-A-empleados.md
```

### empleados-core

Contiene la lógica relacionada con los datos:

- `Empleado.java`: representa la entidad empleado dentro de la aplicación.
- `EmpleadoDAO.java`: contiene las operaciones JDBC para crear, listar, buscar, actualizar y eliminar empleados.

Este módulo se empaqueta como una librería `.jar`.

### empleados-ui

Contiene la interfaz gráfica desarrollada con Swing.

- `MainUI.java`: inicia la aplicación.
- `VentanaPrincipal.java`: contiene la tabla, formulario, validaciones y botones del CRUD.
- `schema.sql`: contiene el script utilizado para crear la base de datos y la tabla.

El módulo `empleados-ui` utiliza `empleados-core` como dependencia.

## Base de datos

El proyecto utiliza MariaDB mediante JDBC.

La base de datos utilizada es:

```text
empleados_db
```

El script para crear la estructura se encuentra en:

```text
empleados-ui/sql/schema.sql
```

La tabla `empleados` utiliza la siguiente estructura general:

| Campo | Tipo |
|---|---|
| id | INT AUTO_INCREMENT PRIMARY KEY |
| nombre_completo | VARCHAR(100) NOT NULL |
| departamento | VARCHAR(100) NOT NULL |
| salario_mensual | DECIMAL(10,2) NOT NULL |
| fecha_contratacion | DATE NOT NULL |
| activo | BOOLEAN NOT NULL DEFAULT TRUE |

## Tecnologías utilizadas

- Java 11
- Maven
- Swing
- JDBC
- MariaDB
- LGoodDatePicker

## Requisitos

Para ejecutar el proyecto se necesita:

- JDK 11 o superior.
- Maven instalado.
- MariaDB disponible localmente.
- La base de datos `empleados_db` creada mediante `schema.sql`.

Las credenciales de conexión pueden configurarse en:

```text
empleados-core/src/main/java/edu/umg/programacion2/proyecto/dao/EmpleadoDAO.java
```

## Compilación

Desde la carpeta raíz del proyecto:

```bash
mvn clean install
```

Maven compilará primero `empleados-core` y posteriormente `empleados-ui`.

## Ejecución

La clase principal de la aplicación es:

```text
edu.umg.programacion2.proyecto.MainUI
```

Puede ejecutarse desde el IDE o mediante Maven:

```bash
mvn -pl empleados-ui exec:java "-Dexec.mainClass=edu.umg.programacion2.proyecto.MainUI"
```

## Flujo general

```text
Usuario
   ↓
Interfaz Swing
   ↓
EmpleadoDAO
   ↓
MariaDB
```

La interfaz gráfica no contiene consultas SQL.  
Las operaciones de persistencia se encuentran centralizadas en `EmpleadoDAO`.

## Operaciones del DAO

`EmpleadoDAO` implementa:

```java
Empleado crear(Empleado empleado)
List<Empleado> listarTodos()
Optional<Empleado> buscarPorId(int id)
boolean actualizar(Empleado empleado)
boolean eliminar(int id)
```

Todas las consultas utilizan `PreparedStatement`.

## Interfaz

La ventana principal incluye:

- Tabla de empleados.
- Campo de nombre.
- Campo de departamento.
- Campo de salario.
- Selector de fecha.
- Estado activo/inactivo.
- Botón Guardar.
- Botón Actualizar.
- Botón Eliminar.
- Botón Limpiar.

La selección de una fila carga los datos del empleado en el formulario para permitir su actualización o eliminación.

## Autor

José Alejandro Cortés Díaz  
Curso: Programación II  
Universidad Mariano Gálvez de Guatemala
