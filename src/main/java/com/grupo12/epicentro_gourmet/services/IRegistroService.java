package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.RegistroUnidadDTO;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;

public interface IRegistroService {

    UnidadDeVenta registrarUnidadConResponsable(RegistroUnidadDTO dto);

}
