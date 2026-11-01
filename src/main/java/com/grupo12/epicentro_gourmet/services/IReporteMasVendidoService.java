package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.PlatoMasVendidoDTO;
import java.util.List;

public interface IReporteMasVendidoService {
    List<PlatoMasVendidoDTO> obtenerTop3PlatosPorUnidadYFestival(Long idFestival, Long idUnidad);
}