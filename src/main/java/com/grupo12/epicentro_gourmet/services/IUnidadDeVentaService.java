package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;

public interface IUnidadDeVentaService {
    UnidadDeVenta buscarPorId(Long idUnidad);
    double calcularCanon(Long idUnidad);
}