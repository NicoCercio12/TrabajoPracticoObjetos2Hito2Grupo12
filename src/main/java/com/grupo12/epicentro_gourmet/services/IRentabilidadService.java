package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.dtos.RentabilidadDiariaDTO;
import java.util.List;

import java.time.LocalDate;

public interface IRentabilidadService {

    double calcularRentabilidadNeta(
            Festival festival,
            UnidadDeVenta unidad);

    double calcularRentabilidadNetaEntreFechas(
            Festival festival,
            UnidadDeVenta unidad,
            LocalDate fechaInicio,
            LocalDate fechaFin);

    List<RentabilidadDiariaDTO> calcularEvolucionDiaria(
            Festival festival,
            UnidadDeVenta unidad,
            LocalDate fechaInicio,
            LocalDate fechaFin);
}
