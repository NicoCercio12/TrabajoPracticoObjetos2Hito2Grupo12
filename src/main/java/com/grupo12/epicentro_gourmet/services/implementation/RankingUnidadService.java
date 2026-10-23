package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.RankingUnidadDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.ItemPedido;
import com.grupo12.epicentro_gourmet.entities.Pedido;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IRankingUnidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class RankingUnidadService implements IRankingUnidadService {

    private final IFestivalRepository festivalRepository;

    public RankingUnidadService(IFestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RankingUnidadDTO> obtenerRankingPorFestival(Long festivalId) {

        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Festival no encontrado"));

        return festival.getLstUdv()
                .stream()
                .map(unidad -> new RankingUnidadDTO(
                        unidad.getId(),
                        unidad.getNombreComercial(),
                        unidad.getCodigoUnico(),
                        calcularRecaudacion(unidad, festivalId)
                ))
                .sorted(
                        Comparator.comparingDouble(
                                RankingUnidadDTO::getRecaudacion
                        ).reversed()
                )
                .toList();
    }

    private double calcularRecaudacion(
            UnidadDeVenta unidad, Long festivalId) {

        double total = 0;

        for (Pedido pedido : unidad.getLstPedidos()) {

            if (pedido.getFestival() == null
                    || !festivalId.equals(pedido.getFestival().getId())) {
                continue;
            }

            for (ItemPedido item : pedido.getLstItems()) {
                if (item.getPlato() != null) {
                    total += item.getPlato().getPrecioVenta()
                            * item.getCantidad();
                }
            }
        }

        return total;
    }
}
