package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.PlatoVendidoDTO;

import java.time.LocalDate;
import java.util.List;

public interface IReportePlatosService {

    List<PlatoVendidoDTO> traerPlatosVendidosEntreFechas(
            Long unidadId,
            LocalDate desde,
            LocalDate hasta,
            String usernameResponsable);
}