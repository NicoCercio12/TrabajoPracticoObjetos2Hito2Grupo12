package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.PlatoMasVendidoDTO;
import com.grupo12.epicentro_gourmet.repositories.IPedidoRepository;
import com.grupo12.epicentro_gourmet.services.IReporteMasVendidoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReporteMasVendidoService implements IReporteMasVendidoService {

    private final IPedidoRepository itemPedidoRepository;

    public ReporteMasVendidoService(IPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlatoMasVendidoDTO> obtenerTop3PlatosPorUnidadYFestival(Long idFestival, Long idUnidad) {
        return itemPedidoRepository.findTopPlatosByUnidadAndFestival(idFestival, idUnidad, PageRequest.of(0, 3));
    }
}