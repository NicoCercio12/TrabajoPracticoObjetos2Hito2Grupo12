package com.grupo12.epicentro_gourmet.dtos;

import com.grupo12.epicentro_gourmet.enums.RolCocina;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CocineroRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String dni;

    @Email
    private String email;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private LocalDate fechaIngreso;

    @NotNull
    private RolCocina rolCocina;

    private Long unidadAsignadaId;

    private Long festivalIdCalculo;
}