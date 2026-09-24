package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import javax.swing.ListSelectionModel;
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
    private final JButton botonActualizar;
    private final JButton botonEliminar;
    private final JButton botonLimpiar;

    private Integer idEmpleadoSeleccionado;

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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEmpleados = new JTable(modeloTabla);
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaEmpleados);
        add(scrollTabla, BorderLayout.CENTER);

        // ==========================================
        // CAMPOS DEL FORMULARIO
        // ==========================================

        campoNombre = new JTextField();
        campoDepartamento = new JTextField();
        campoSalario = new JTextField();

        DatePickerSettings configuracionFecha = new DatePickerSettings();
        configuracionFecha.setFormatForDatesCommonEra("yyyy-MM-dd");

        campoFecha = new DatePicker(configuracionFecha);
        campoFecha.setDate(LocalDate.now());

        checkActivo = new JCheckBox("Empleado activo", true);

        // ==========================================
        // PANEL DEL FORMULARIO
        // ==========================================

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del empleado"));

        panelFormulario.add(new JLabel("Nombre completo:"));
        panelFormulario.add(campoNombre);

        panelFormulario.add(new JLabel("Departamento:"));
        panelFormulario.add(campoDepartamento);

        panelFormulario.add(new JLabel("Salario mensual:"));
        panelFormulario.add(campoSalario);

        panelFormulario.add(new JLabel("Fecha contratación:"));
        panelFormulario.add(campoFecha);

        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(checkActivo);

        // ==========================================
        // BOTONES
        // ==========================================

        botonGuardar = new JButton("Guardar");
        botonActualizar = new JButton("Actualizar");
        botonEliminar = new JButton("Eliminar");
        botonLimpiar = new JButton("Limpiar");

        botonActualizar.setEnabled(false);
        botonEliminar.setEnabled(false);

        JPanel panelBotones = new JPanel(new FlowLayout());

        panelBotones.add(botonGuardar);
        panelBotones.add(botonActualizar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonLimpiar);

        // ==========================================
        // PANEL INFERIOR
        // ==========================================

        JPanel panelInferior = new JPanel(new BorderLayout());

        panelInferior.add(panelFormulario, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // ==========================================
        // EVENTOS
        // ==========================================

        botonGuardar.addActionListener(e -> guardarEmpleado());
        botonActualizar.addActionListener(e -> actualizarEmpleado());
        botonEliminar.addActionListener(e -> eliminarEmpleado());
        botonLimpiar.addActionListener(e -> limpiarFormulario());

        tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarEmpleadoSeleccionado();
            }
        });

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

            List<Empleado> empleados = empleadoDAO.listarTodos();
            modeloTabla.setRowCount(0);

            for (Empleado empleado : empleados) {

                modeloTabla.addRow(
                        new Object[]{
                                empleado.getId(),
                                empleado.getNombreCompleto(),
                                empleado.getDepartamento(),
                                empleado.getSalarioMensual(),
                                empleado.getFechaContratacion(),
                                empleado.isActivo() ? "Activo" : "Inactivo"
                        }
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar empleados: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // CARGAR EMPLEADO SELECCIONADO
    // ==========================================

    private void cargarEmpleadoSeleccionado() {

        int filaSeleccionada = tablaEmpleados.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        int id = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);

        try {

            Optional<Empleado> empleado = empleadoDAO.buscarPorId(id);

            if (!empleado.isPresent()) {
                mostrarError("El empleado seleccionado ya no existe.");
                cargarEmpleados();
                return;
            }

            Empleado seleccionado = empleado.get();

            idEmpleadoSeleccionado = seleccionado.getId();

            campoNombre.setText(seleccionado.getNombreCompleto());
            campoDepartamento.setText(seleccionado.getDepartamento());
            campoSalario.setText(seleccionado.getSalarioMensual().toString());
            campoFecha.setDate(seleccionado.getFechaContratacion());
            checkActivo.setSelected(seleccionado.isActivo());

            botonGuardar.setEnabled(false);
            botonActualizar.setEnabled(true);
            botonEliminar.setEnabled(true);
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al buscar empleado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // OBTENER Y VALIDAR DATOS DEL FORMULARIO
    // ==========================================

    private Empleado obtenerEmpleadoFormulario(int id) {

        String nombre = campoNombre.getText().trim();
        String departamento = campoDepartamento.getText().trim();
        String salarioTexto = campoSalario.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede quedar vacío.");
            return null;
        }

        if (departamento.isEmpty()) {
            mostrarError("El departamento no puede quedar vacío.");
            return null;
        }

        BigDecimal salario;

        try {
            salario = new BigDecimal(salarioTexto);
        } catch (NumberFormatException e) {
            mostrarError("El salario debe ser un número válido.");
            return null;
        }

        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarError("El salario debe ser mayor a cero.");
            return null;
        }

        LocalDate fechaContratacion = campoFecha.getDate();

        if (fechaContratacion == null) {
            mostrarError("Debe seleccionar una fecha de contratación.");
            return null;
        }

        if (fechaContratacion.isAfter(LocalDate.now())) {
            mostrarError("La fecha de contratación no puede ser futura.");
            return null;
        }

        boolean activo = checkActivo.isSelected();

        if (id == 0) {
            return new Empleado(
                    nombre,
                    departamento,
                    salario,
                    fechaContratacion,
                    activo
            );
        }

        return new Empleado(
                id,
                nombre,
                departamento,
                salario,
                fechaContratacion,
                activo
        );
    }

    // ==========================================
    // GUARDAR EMPLEADO
    // ==========================================

    private void guardarEmpleado() {

        Empleado nuevoEmpleado = obtenerEmpleadoFormulario(0);

        if (nuevoEmpleado == null) {
            return;
        }

        try {

            Empleado empleadoCreado = empleadoDAO.crear(nuevoEmpleado);

            JOptionPane.showMessageDialog(
                    this,
                    "Empleado registrado con ID " + empleadoCreado.getId(),
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarEmpleados();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar empleado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // ACTUALIZAR EMPLEADO
    // ==========================================

    private void actualizarEmpleado() {

        if (idEmpleadoSeleccionado == null) {
            mostrarError("Debe seleccionar un empleado.");
            return;
        }

        Empleado empleadoActualizado = obtenerEmpleadoFormulario(idEmpleadoSeleccionado);

        if (empleadoActualizado == null) {
            return;
        }

        try {

            boolean actualizado = empleadoDAO.actualizar(empleadoActualizado);

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Empleado actualizado correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limpiarFormulario();
                cargarEmpleados();

            } else {
                mostrarError("No se pudo actualizar el empleado.");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar empleado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // ELIMINAR EMPLEADO
    // ==========================================

    private void eliminarEmpleado() {

        if (idEmpleadoSeleccionado == null) {
            mostrarError("Debe seleccionar un empleado.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este empleado?\n"
                        + "Esta acción eliminará el registro permanentemente.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean eliminado = empleadoDAO.eliminar(idEmpleadoSeleccionado);

            if (eliminado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Empleado eliminado correctamente.",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limpiarFormulario();
                cargarEmpleados();

            } else {
                mostrarError("No se pudo eliminar el empleado.");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar empleado: " + e.getMessage(),
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

        idEmpleadoSeleccionado = null;

        tablaEmpleados.clearSelection();

        botonGuardar.setEnabled(true);
        botonActualizar.setEnabled(false);

        campoNombre.requestFocus();
    }

    // ==========================================
    // MOSTRAR ERROR DE VALIDACIÓN
    // ==========================================

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Datos inválidos",
                JOptionPane.WARNING_MESSAGE
        );
    }
}