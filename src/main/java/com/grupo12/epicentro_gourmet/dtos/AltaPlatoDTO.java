package com.grupo12.epicentro_gourmet.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AltaPlatoDTO {

    @NotBlank(message = "El nombre del plato es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "El precio de venta debe ser mayor a cero")
    private Double precioVenta;

    @NotNull(message = "El costo de producción es obligatorio")
    @DecimalMin(
            value = "0.0",
            message = "El costo de producción debe ser cero o mayor")
    private Double costoProduccion;

    @NotNull(message = "Debés seleccionar una unidad de venta")
    @Positive(message = "La unidad de venta debe ser válida")
    private Long idUnidad;
}