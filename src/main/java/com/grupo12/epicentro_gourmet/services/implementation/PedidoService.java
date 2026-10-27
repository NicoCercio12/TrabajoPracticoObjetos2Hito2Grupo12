package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.PlatoDTO;
import com.grupo12.epicentro_gourmet.entities.Plato;
import com.grupo12.epicentro_gourmet.repositories.IPedidoRepository;
import com.grupo12.epicentro_gourmet.services.IPedidoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    private final IPedidoRepository repository;

    public PedidoService(IPedidoRepository repository) {
        this.repository = repository;
    }

    public PlatoDTO obtenerPlatoEstrella(Long festivalId, Long unidadId) {
        // Pedimos solo el primer resultado (el de mayor cantidad) usando PageRequest
        List<Plato> platos = repository.encontrarPlatoEstrella(festivalId, unidadId, PageRequest.of(0, 1));

        if (platos == null || platos.isEmpty()) {
            return null; // O un DTO vacío si no hay ventas registradas
        }

        Plato plato = platos.get(0);

        // Mapeamos al DTO
        return PlatoDTO.builder()
                .idPlato(plato.getIdPlato())
                .nombre(plato.getNombre())
                .precioVenta(plato.getPrecioVenta())
                .costoProduccion(plato.getCostoProduccion())
                .build();
    }
}