package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Plato;
import com.grupo12.epicentro_gourmet.dtos.PlatoListadoDTO;
import java.util.List;

public interface IPlatoService {

    Plato darDeAlta(String nombre, double precioVenta, double costoProduccion, Long idUnidad, String usernameResponsable);

    List<PlatoListadoDTO> listarPorResponsable(String usernameResponsable);
}
