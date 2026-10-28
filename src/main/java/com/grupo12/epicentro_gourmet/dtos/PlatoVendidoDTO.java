package com.grupo12.epicentro_gourmet.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlatoVendidoDTO {

    private Long idPlato;
    private String nombre;
    private Long cantidadVendida;
}