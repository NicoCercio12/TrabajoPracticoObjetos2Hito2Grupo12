package com.grupo12.epicentro_gourmet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public abstract double calcularSueldo(Festival festival);
    //aca calculamos esMayorDeEdad, calcularAntiguedad, darDeBaja, reactivar...
}