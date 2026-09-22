package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.umg.programacion2.proyecto.dao.EmpleadoDAO;
import edu.umg.programacion2.proyecto.modelo.Empleado;

public class VentanaPrincipal extends JFrame {

    private final EmpleadoDAO empleadoDAO;

    private final DefaultTableModel modeloTabla;
    private final JTable tablaEmpleados;

    public VentanaPrincipal() {

        empleadoDAO = new EmpleadoDAO();

        setTitle("Gestión de empleados");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre completo",
                        "Departamento",
                        "Salario",
                        "Fecha de contratación",
                        "Activo"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tablaEmpleados = new JTable(modeloTabla);

        JScrollPane scrollPane =
                new JScrollPane(tablaEmpleados);

        add(scrollPane, BorderLayout.CENTER);

        cargarEmpleados();
    }

    private void cargarEmpleados() {

        try {

            List<Empleado> empleados =
                    empleadoDAO.listarTodos();

            modeloTabla.setRowCount(0);

            for (Empleado empleado : empleados) {

                modeloTabla.addRow(
                        new Object[]{
                                empleado.getId(),
                                empleado.getNombreCompleto(),
                                empleado.getDepartamento(),
                                empleado.getSalarioMensual(),
                                empleado.getFechaContratacion(),
                                empleado.isActivo()
                                        ? "Activo"
                                        : "Inactivo"
                        }
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar empleados: "
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}