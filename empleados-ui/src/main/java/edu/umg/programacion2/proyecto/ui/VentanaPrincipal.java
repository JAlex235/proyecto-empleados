package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import edu.umg.programacion2.proyecto.dao.EmpleadoDAO;
import edu.umg.programacion2.proyecto.modelo.Empleado;

public class VentanaPrincipal extends JFrame {

    private final EmpleadoDAO empleadoDAO;

    private final DefaultTableModel modeloTabla;
    private final JTable tablaEmpleados;

    private final JTextField campoNombre;
    private final JTextField campoDepartamento;
    private final JTextField campoSalario;

    private final DatePicker campoFecha;

    private final JCheckBox checkActivo;

    private final JButton botonGuardar;
    private final JButton botonLimpiar;

    public VentanaPrincipal() {

        empleadoDAO = new EmpleadoDAO();

        setTitle("Gestión de empleados");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ==========================================
        // TABLA DE EMPLEADOS
        // ==========================================

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

        JScrollPane scrollTabla =new JScrollPane(tablaEmpleados);

        add(
                scrollTabla,
                BorderLayout.CENTER
        );

        // ==========================================
        // CAMPOS DEL FORMULARIO
        // ==========================================

        campoNombre = new JTextField();

        campoDepartamento = new JTextField();

        campoSalario = new JTextField();

        DatePickerSettings configuracionFecha = new DatePickerSettings();

        configuracionFecha.setFormatForDatesCommonEra("yyyy-MM-dd");

        campoFecha =new DatePicker(configuracionFecha);

        campoFecha.setDate(LocalDate.now());

        checkActivo = new JCheckBox("Empleado activo", true);

        // ==========================================
        // PANEL DEL FORMULARIO
        // ==========================================

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));

        panelFormulario.setBorder( BorderFactory.createTitledBorder("Datos del empleado"));

        panelFormulario.add( new JLabel("Nombre completo:"));

        panelFormulario.add(campoNombre);

        panelFormulario.add( new JLabel("Departamento:"));

        panelFormulario.add(campoDepartamento);

        panelFormulario.add( new JLabel("Salario mensual:"));

        panelFormulario.add(campoSalario);

        panelFormulario.add( new JLabel("Fecha contratación:"));

        panelFormulario.add(campoFecha);

        panelFormulario.add( new JLabel("Estado:"));

        panelFormulario.add(checkActivo);

        // ==========================================
        // BOTONES
        // ==========================================

        botonGuardar = new JButton("Guardar");

        botonLimpiar = new JButton("Limpiar");

        JPanel panelBotones =new JPanel( new FlowLayout());

        panelBotones.add(botonGuardar);

        panelBotones.add(botonLimpiar);

        // ==========================================
        // PANEL INFERIOR
        // ==========================================

        JPanel panelInferior = new JPanel( new BorderLayout());

        panelInferior.add(panelFormulario, BorderLayout.CENTER);

        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(
                panelInferior,
                BorderLayout.SOUTH
        );

        // ==========================================
        // EVENTOS
        // ==========================================

        botonGuardar.addActionListener(
                e -> guardarEmpleado()
        );

        botonLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        // ==========================================
        // CARGA INICIAL
        // ==========================================

        cargarEmpleados();
    }

    // ==========================================
    // CARGAR EMPLEADOS EN LA TABLA
    // ==========================================

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

    // ==========================================
    // GUARDAR EMPLEADO
    // ==========================================

    private void guardarEmpleado() {

        String nombre = campoNombre.getText().trim();

        String departamento = campoDepartamento.getText().trim();

        String salarioTexto =campoSalario.getText().trim();

        // ==========================================
        // VALIDAR NOMBRE
        // ==========================================

        if (nombre.isEmpty()) {

            mostrarError("El nombre no puede quedar vacío.");

            return;
        }

        // ==========================================
        // VALIDAR DEPARTAMENTO
        // ==========================================

        if (departamento.isEmpty()) {

            mostrarError("El departamento no puede quedar vacío.");

            return;
        }

        // ==========================================
        // VALIDAR SALARIO
        // ==========================================

        BigDecimal salario;

        try {

            salario =new BigDecimal(salarioTexto);

        } catch (NumberFormatException e) {

            mostrarError("El salario debe ser un número válido.");

            return;
        }

        if (salario.compareTo(BigDecimal.ZERO) <= 0) {

            mostrarError( "El salario debe ser mayor a cero.");

            return;
        }

        // ==========================================
        // OBTENER FECHA DEL DATE PICKER
        // ==========================================

        LocalDate fechaContratacion =
                campoFecha.getDate();

        if (fechaContratacion == null) {

            mostrarError("Debe seleccionar una fecha de contratación.");

            return;
        }

        // ==========================================
        // VALIDAR FECHA FUTURA
        // ==========================================

        if (fechaContratacion.isAfter(
                LocalDate.now()
        )) {

            mostrarError(
                    "La fecha de contratación no puede ser futura."
            );

            return;
        }

        // ==========================================
        // OBTENER ESTADO
        // ==========================================

        boolean activo = checkActivo.isSelected();

        // ==========================================
        // CREAR OBJETO EMPLEADO
        // ==========================================

        Empleado nuevoEmpleado =
                new Empleado(
                        nombre,
                        departamento,
                        salario,
                        fechaContratacion,
                        activo
                );

        // ==========================================
        // GUARDAR EN LA BASE DE DATOS
        // ==========================================

        try {

            Empleado empleadoCreado = empleadoDAO.crear(nuevoEmpleado);

            JOptionPane.showMessageDialog(
                    this,
                    "Empleado registrado con ID "
                            + empleadoCreado.getId(),
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            cargarEmpleados();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar empleado: "
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // LIMPIAR FORMULARIO
    // ==========================================

    private void limpiarFormulario() {

        campoNombre.setText("");

        campoDepartamento.setText("");

        campoSalario.setText("");

        campoFecha.setDate(LocalDate.now());

        checkActivo.setSelected(true);

        campoNombre.requestFocus();
    }

    // ==========================================
    // MOSTRAR ERROR DE VALIDACIÓN
    // ==========================================

    private void mostrarError(
            String mensaje
    ) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Datos inválidos",
                JOptionPane.WARNING_MESSAGE
        );
    }
}