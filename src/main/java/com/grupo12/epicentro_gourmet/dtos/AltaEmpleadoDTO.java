package com.grupo12.epicentro_gourmet.dtos;

import com.grupo12.epicentro_gourmet.enums.RolCocina;
import com.grupo12.epicentro_gourmet.enums.TipoEmpleado;
import com.grupo12.epicentro_gourmet.enums.Turno;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AltaEmpleadoDTO {

    @NotBlank(message = "Ingresá el nombre")
    private String nombre;

    @NotBlank(message = "Ingresá el apellido")
    private String apellido;

    @NotBlank(message = "Ingresá el DNI")
    private String dni;

    @NotBlank(message = "Ingresá el email")
    @Email(message = "Ingresá un email válido")
    @Size(max = 45, message = "El email no puede superar los 45 caracteres")
    private String email;

    @NotNull(message = "Ingresá la fecha de nacimiento")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Ingresá la fecha de ingreso")
    private LocalDate fechaIngreso;

    @NotNull(message = "Seleccioná el tipo de empleado")
    private TipoEmpleado tipoEmpleado;

    private Turno turno;

    private RolCocina rolCocina;

    @NotNull(message = "Seleccioná una unidad de venta")
    @Positive(message = "La unidad de venta debe ser válida")
    private Long idUnidad;
}