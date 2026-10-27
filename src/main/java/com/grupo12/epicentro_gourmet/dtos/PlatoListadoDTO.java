package com.grupo12.epicentro_gourmet.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlatoListadoDTO {

    private Long idPlato;
    private String nombre;
    private double precioVenta;
    private double costoProduccion;
    private Long idUnidad;
    private String nombreUnidad;
}