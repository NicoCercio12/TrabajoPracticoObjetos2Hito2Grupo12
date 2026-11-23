package com.grupo12.epicentro_gourmet.dtos;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;

@JsonPropertyOrder({
    "idEmpleado",
    "nombre",
    "apellido",
    "dni",
    "email",
    "fechaNacimiento",
    "fechaIngreso",
    "antiguedad"
})
public class EmpleadoUnidadDTO {
    private long idEmpleado;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private LocalDate fechaNacimiento;
    private LocalDate fechaIngreso;
    private int antiguedad;


    
    public EmpleadoUnidadDTO() {
    }

    public long getIdEmpleado() {
        return idEmpleado;
    }
    public void setIdEmpleado(long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    public int getAntiguedad() {
        return antiguedad;
    }
    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }


    
}
