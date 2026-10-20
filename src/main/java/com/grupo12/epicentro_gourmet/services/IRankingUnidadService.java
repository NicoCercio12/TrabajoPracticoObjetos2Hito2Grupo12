package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.RankingUnidadDTO;
import java.util.List;

public interface IRankingUnidadService {
    List<RankingUnidadDTO> obtenerRankingPorFestival(Long festivalId);
}
