package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;

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
}
