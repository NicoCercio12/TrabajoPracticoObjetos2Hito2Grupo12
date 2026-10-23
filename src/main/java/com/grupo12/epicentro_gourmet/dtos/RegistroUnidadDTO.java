package com.grupo12.epicentro_gourmet.dtos;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.grupo12.epicentro_gourmet.enums.RolCocina;
import com.grupo12.epicentro_gourmet.enums.TipoEmpleado;
import com.grupo12.epicentro_gourmet.enums.TipoUnidad;
import com.grupo12.epicentro_gourmet.enums.Turno;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroUnidadDTO {

    // Responsable (solo se pide la primera vez; lo valida RegistroService)
    private String nombre;
    private String apellido;
    private String dni;

    @Email(message = "El email no es válido")
    private String email;

    @Past(message = "La fecha de nacimiento debe ser pasada")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    private TipoEmpleado tipoEmpleado;
    private RolCocina rolCocina;
    private Turno turno;

    // Unidad de venta
    @NotBlank(message = "El nombre comercial es obligatorio")
    private String nombreComercial;

    @NotBlank(message = "El código único es obligatorio")
    private String codigoUnico;

    @NotNull(message = "La superficie es obligatoria")
    @Positive(message = "La superficie debe ser mayor a 0")
    private Double superficie;

    @NotNull(message = "Elegí el tipo de unidad")
    private TipoUnidad tipoUnidad;

    // Food truck
    private String patente;
    private boolean requiereElectricidad;

    // Puesto desarmable
    private Integer cantCarpas;
    private Integer tiempoMontajeMinutos;
}