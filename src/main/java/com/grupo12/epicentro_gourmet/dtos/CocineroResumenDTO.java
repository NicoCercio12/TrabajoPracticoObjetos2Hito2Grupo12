package com.grupo12.epicentro_gourmet.dtos;

import com.grupo12.epicentro_gourmet.enums.RolCocina;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CocineroResumenDTO {

    private long idEmpleado;
    private String nombre;
    private String apellido;
    private String dni;
    private boolean activo;
    private RolCocina rolCocina;
    private Long unidadAsignadaId;
    private String nombreUnidadAsignada;
    private int antiguedadAnios;
    private LocalDate fechaIngreso;
    private double sueldoEstimado;
}