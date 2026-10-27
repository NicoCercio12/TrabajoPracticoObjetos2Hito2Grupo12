package com.grupo12.epicentro_gourmet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatoDTO {
    private Long idPlato;
    private String nombre;
    private double precioVenta;
    private double costoProduccion;
}