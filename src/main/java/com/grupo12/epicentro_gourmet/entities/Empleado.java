package com.grupo12.epicentro_gourmet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Getter
@Setter 
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long idEmpleado;


    private String nombre;
    private String apellido;

    @Column(unique = true, nullable = false)
    private String dni;

    private String email;
    private LocalDate fechaNacimiento;
    private LocalDate fechaIngreso;
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadDeVenta unidadAsignada;

    @OneToOne(fetch = FetchType.LAZY)
    private User usuario;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected Empleado(String nombre, String apellido, String dni, String email,
                       LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaIngreso = fechaIngreso;
    }

    public abstract double calcularSueldo(Festival festival);

    //Lo termino - MatiFunes
    public boolean esMayorDeEdad() {
        boolean mayorDeEdad = false;

        if(fechaNacimiento != null){
            int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
            mayorDeEdad = edad >= 18;
        }
        return mayorDeEdad;
    }

    public void darDeBaja() {
        this.activo = false;
    }

    public void reactivar() {
        this.activo = true;
    }


    //liqi de haberes
    public int calcularAntiguedad() {
        if (fechaIngreso == null) return 0;
        return java.time.Period.between(fechaIngreso, java.time.LocalDate.now()).getYears();
    }

    protected double calcularMultiplicadorDias(long dias) {
        return ((int) (dias / 15) + 1) * 0.5;
    }
}