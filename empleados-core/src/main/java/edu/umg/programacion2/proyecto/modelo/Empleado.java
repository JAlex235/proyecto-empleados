package edu.umg.programacion2.proyecto.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Empleado {

    private final int id;
    private final String nombreCompleto;
    private final String departamento;
    private final BigDecimal salarioMensual;
    private final LocalDate fechaContratacion;
    private final boolean activo;
    private final int aniosExperiencia;

    public Empleado(int id, String nombreCompleto, String departamento,
                    BigDecimal salarioMensual, LocalDate fechaContratacion,
                    boolean activo, int aniosExperiencia) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salarioMensual = salarioMensual;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
        this.aniosExperiencia = aniosExperiencia;
    }

    public Empleado(String nombreCompleto, String departamento,
                    BigDecimal salarioMensual, LocalDate fechaContratacion,
                    boolean activo, int aniosExperiencia) {
        this(0, nombreCompleto, departamento, salarioMensual,
                fechaContratacion, activo, aniosExperiencia);
    }

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDepartamento() {
        return departamento;
    }

    public BigDecimal getSalarioMensual() {
        return salarioMensual;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | Q%s | %s | %s | %d",
                id,
                nombreCompleto,
                departamento,
                salarioMensual,
                fechaContratacion,
                activo ? "Activo" : "Inactivo",
                aniosExperiencia);                
    }
}