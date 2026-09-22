# Variante A — Gestión de empleados

## Descripción conceptual del dominio

Una empresa pequeña necesita un sistema para llevar el registro de sus
empleados. Cada empleado que se registra necesita:

- Un identificador único que el sistema le asigna automáticamente al
  crearlo (el empleado no lo elige, no se repite nunca).
- Su nombre completo.
- El departamento al que pertenece (por ejemplo: Ventas, Sistemas,
  Contabilidad, Recursos Humanos — la empresa no tiene una lista cerrada de
  departamentos, se escribe como texto libre).
- Su salario mensual, que siempre es un monto positivo y puede tener
  centavos.
- La fecha en la que fue contratado.
- Si actualmente sigue activo en la empresa o ya no trabaja ahí (un
  empleado que se retira no se borra del sistema, solo deja de estar
  activo).

## Reglas de negocio

- El nombre no puede quedar vacío.
- El departamento no puede quedar vacío.
- El salario debe ser mayor a cero — no se acepta cero ni negativos.
- La fecha de contratación no puede ser una fecha futura.
- Un empleado "inactivo" sigue existiendo en el sistema (para historial),
  simplemente no cuenta como personal activo.

## Lo que se espera ver en pantalla

- Listado de todos los empleados con su nombre, departamento, salario y si
  están activos.
- Formulario para registrar un empleado nuevo.
- Formulario para editar los datos de un empleado existente (el campo
  "activo" se edita ahí como cualquier otro campo).
- Acción para eliminar un empleado: un borrado físico de la fila, con
  confirmación antes de aplicarlo (igual que en `clase08-productos-crud`).
  El campo "activo" no está pensado para reemplazar esta acción — son dos
  cosas distintas: "activo" es un dato del empleado, "eliminar" es quitarlo
  del sistema por completo.

## Ejemplo de interacción esperada

```
Nombre completo: Ana Lucía Pérez
Departamento: Sistemas
Salario mensual: 8500.00
Fecha de contratación: 2024-03-15
Empleado registrado.

Listado:
[1] Ana Lucía Pérez     | Sistemas      | Q8500.00 | Activo
[2] Carlos Roberto Mux  | Ventas        | Q6200.00 | Activo
[3] Diana Sofía Cabrera | Contabilidad  | Q7100.00 | Inactivo
```

No se te da el tipo de dato de cada columna a propósito: decide tú si el
salario es `DECIMAL`, cuántos dígitos necesita, si el nombre necesita 50 o
100 caracteres, etc. Documenta esas decisiones en tu `sql/schema.sql`.
