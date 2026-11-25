package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.CocineroRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.CocineroResumenDTO;
import com.grupo12.epicentro_gourmet.enums.RolCocina;

import java.util.List;

public interface ICocineroService {

    CocineroResumenDTO crearCocinero(CocineroRequestDTO requestDTO);

    List<CocineroResumenDTO> buscarCocineros(
            Long unidadId,
            RolCocina rolCocina,
            Boolean activo,
            Long festivalId
    );
}