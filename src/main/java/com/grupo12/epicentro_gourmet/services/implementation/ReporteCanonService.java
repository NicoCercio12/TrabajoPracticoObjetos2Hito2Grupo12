
package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.ReporteMayoresCanonDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.services.IReporteCanonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteCanonService implements IReporteCanonService {

    @Override
    @Transactional(readOnly = true)
    public List<ReporteMayoresCanonDTO> obtenerMayoresCanon(
            Festival festival, int cantidad) {

        if (festival == null) {
            throw new IllegalArgumentException(
                    "El festival no puede ser nulo");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        return festival.getLstUdv()
                .stream()
                .sorted(Comparator.comparingDouble(
                        UnidadDeVenta::calcularCanon).reversed())
                .limit(cantidad)
                .map(unidad -> new ReporteMayoresCanonDTO(
                        unidad.getNombreComercial(),
                        unidad.getCodigoUnico(),
                        unidad.getClass().getSimpleName(),
                        unidad.calcularCanon()
                ))
                .collect(Collectors.toList());
    }
}

