package com.grupo12.epicentro_gourmet.services;
import com.grupo12.epicentro_gourmet.dtos.UnidadDeVentaDTO;
import com.grupo12.epicentro_gourmet.dtos.ModificarPlatoDeUnidadResponseDTO;

import java.util.List;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
public interface IUnidadDeVentaService {

    // Devuelve la lista de unidades de venta asociadas al festival indicado por su ID
    List<UnidadDeVentaDTO> obtenerUnidadesPorFestivalId(Long festivalId);
    UnidadDeVenta buscarPorId(Long idUnidad);
    double calcularCanon(Long idUnidad);

    //Metodo que permite a un cliente(tomese  a cliente como otro servicio externo a la aplicacion) modificar el precio de un plato de una unidad de venta.
    ModificarPlatoDeUnidadResponseDTO modificarPlatoDeUnaUnidad(String codigoUnico,String nombrePlato, double precioPlato);
   
}

