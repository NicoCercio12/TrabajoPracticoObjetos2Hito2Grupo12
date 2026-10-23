package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.ReporteMayoresCanonDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import java.util.List;

public interface IReporteCanonService {
    List<ReporteMayoresCanonDTO> obtenerMayoresCanon(Festival festival, int cantidad);
}
