package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.dtos.PedidoRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.PedidoResumenDTO;
import com.grupo12.epicentro_gourmet.dtos.PlatoDTO;

import java.time.LocalDate;
import java.util.List;

public interface IPedidoService {
    PlatoDTO obtenerPlatoEstrella(Long festivalId, Long unidadId);

    PedidoResumenDTO crearPedido(PedidoRequestDTO requestDTO);

    List<PedidoResumenDTO> buscarPedidosConFiltros(
            Long festivalId,
            Long unidadId,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );

    PedidoResumenDTO actualizarPedido(Long idPedido, PedidoRequestDTO requestDTO);

    void eliminarPedido(Long idPedido);
}