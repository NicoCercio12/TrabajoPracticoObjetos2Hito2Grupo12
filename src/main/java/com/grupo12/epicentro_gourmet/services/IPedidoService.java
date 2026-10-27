package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.PlatoDTO;

public interface IPedidoService {
    PlatoDTO obtenerPlatoEstrella(Long festivalId, Long unidadId);
}