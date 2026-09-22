package edu.umg.programacion2.proyecto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.umg.programacion2.proyecto.modelo.Empleado;

public class EmpleadoDAO {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/empleados_db";

    private static final String USUARIO = "root";
    private static final String PASSWORD = "Progra2";

    // Crear un nuevo empleado
    public Empleado crear(Empleado empleado) throws SQLException {

        String sql = "INSERT INTO empleados "
                + "(nombre_completo, departamento, salario_mensual, "
                + "fecha_contratacion, activo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion =
                    DriverManager.getConnection(URL, USUARIO, PASSWORD);

            PreparedStatement statement =
                    conexion.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, empleado.getNombreCompleto());
            statement.setString(2, empleado.getDepartamento());
            statement.setBigDecimal(3, empleado.getSalarioMensual());

            statement.setDate(
                    4,
                    java.sql.Date.valueOf(
                            empleado.getFechaContratacion()
                    )
            );

            statement.setBoolean(5, empleado.isActivo());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {

                if (keys.next()) {

                    int idGenerado = keys.getInt(1);

                    return new Empleado(
                            idGenerado,
                            empleado.getNombreCompleto(),
                            empleado.getDepartamento(),
                            empleado.getSalarioMensual(),
                            empleado.getFechaContratacion(),
                            empleado.isActivo()
                    );
                }
            }
        }

        throw new SQLException(
                "No se pudo obtener el ID del empleado creado."
        );
    }

    // Listar todos los empleados
    public List<Empleado> listarTodos() throws SQLException {

        List<Empleado> empleados = new ArrayList<>();

        String sql = "SELECT id, nombre_completo, departamento, "
                + "salario_mensual, fecha_contratacion, activo "
                + "FROM empleados "
                + "ORDER BY id";

        try (Connection conexion =
                    DriverManager.getConnection(URL, USUARIO, PASSWORD);

            PreparedStatement statement =
                    conexion.prepareStatement(sql);

            ResultSet data =
                    statement.executeQuery()) {

            while (data.next()) {
                empleados.add(mapearFila(data));
            }
        }

        return empleados;
    }

    // Buscar un empleado por su ID
    public Optional<Empleado> buscarPorId(int id) throws SQLException {

        String sql = "SELECT id, nombre_completo, departamento, "
                + "salario_mensual, fecha_contratacion, activo "
                + "FROM empleados "
                + "WHERE id = ?";

        try (Connection conexion =
                    DriverManager.getConnection(URL, USUARIO, PASSWORD);

            PreparedStatement statement =
                    conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet data = statement.executeQuery()) {

                if (data.next()) {
                    return Optional.of(mapearFila(data));
                }
            }
        }

        return Optional.empty();
    }

    // Actualizar los datos de un empleado
    public boolean actualizar(Empleado empleado) throws SQLException {

        String sql = "UPDATE empleados "
                + "SET nombre_completo = ?, "
                + "departamento = ?, "
                + "salario_mensual = ?, "
                + "fecha_contratacion = ?, "
                + "activo = ? "
                + "WHERE id = ?";

        try (Connection conexion =
                    DriverManager.getConnection(URL, USUARIO, PASSWORD);

            PreparedStatement statement =
                    conexion.prepareStatement(sql)) {

            statement.setString(1, empleado.getNombreCompleto());
            statement.setString(2, empleado.getDepartamento());
            statement.setBigDecimal(3, empleado.getSalarioMensual());

            statement.setDate(
                    4,
                    java.sql.Date.valueOf(
                            empleado.getFechaContratacion()
                    )
            );

            statement.setBoolean(5, empleado.isActivo());
            statement.setInt(6, empleado.getId());

            return statement.executeUpdate() > 0;
        }
    }

    // Eliminar físicamente un empleado por su ID
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM empleados "
                + "WHERE id = ?";

        try (Connection conexion =
                    DriverManager.getConnection(URL, USUARIO, PASSWORD);

            PreparedStatement statement =
                    conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    // Convierte una fila del ResultSet en un objeto Empleado
    private Empleado mapearFila(ResultSet data)
            throws SQLException {

        int id = data.getInt("id");

        String nombreCompleto =
                data.getString("nombre_completo");

        String departamento =
                data.getString("departamento");

        BigDecimal salarioMensual =
                data.getBigDecimal("salario_mensual");

        LocalDate fechaContratacion =
                data.getDate("fecha_contratacion")
                        .toLocalDate();

        boolean activo =
                data.getBoolean("activo");

        return new Empleado(
                id,
                nombreCompleto,
                departamento,
                salarioMensual,
                fechaContratacion,
                activo
        );
    }
}