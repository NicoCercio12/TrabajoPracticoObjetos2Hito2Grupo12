package com.grupo12.epicentro_gourmet.services;
import com.grupo12.epicentro_gourmet.dtos.UnidadDeVentaDTO;
import java.util.List;
public interface IUnidadDeVentaService {

    // Devuelve la lista de unidades de venta asociadas al festival indicado por su ID
    List<UnidadDeVentaDTO> obtenerUnidadesPorFestivalId(Long festivalId);

   
}
