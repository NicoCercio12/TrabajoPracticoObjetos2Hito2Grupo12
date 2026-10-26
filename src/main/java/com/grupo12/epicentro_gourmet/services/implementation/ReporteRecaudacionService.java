package com.grupo12.epicentro_gourmet.services.implementation;
import com.grupo12.epicentro_gourmet.dtos.ReporteVentaDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteRecaudacionService {

    @Autowired
    private IFestivalRepository festivalRepository;

    @Autowired
    private IPedidoRepository pedidoRepository;

    public ReporteVentaDTO obtenerReporteRecaudacion(Long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new RuntimeException("Festival no encontrado"));

        Double total = pedidoRepository.calcularRecaudacionTotalPorFestival(festivalId);
        double recaudacion = (total != null) ? total : 0.0;

        return new ReporteVentaDTO(festival.getId(), festival.getNombre(), recaudacion);
    }

    public List<Festival> listarFestivales() {
        return festivalRepository.findAll();
    }
}