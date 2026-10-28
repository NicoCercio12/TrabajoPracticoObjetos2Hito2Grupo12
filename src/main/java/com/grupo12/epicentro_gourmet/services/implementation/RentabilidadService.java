package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IPedidoRepository;
import com.grupo12.epicentro_gourmet.services.IRentabilidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)

public class RentabilidadService implements IRentabilidadService {

    private final IPedidoRepository pedidoRepository;
    private final IEmpleadoRepository empleadoRepository;

    public RentabilidadService(
            IPedidoRepository pedidoRepository,
            IEmpleadoRepository empleadoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public double calcularRentabilidadNeta(
            Festival festival,
            UnidadDeVenta unidad) {

        validarFestivalYUnidad(festival, unidad);

        Long idFestival = festival.getId();
        Long idUnidad = unidad.getId();

        double recaudacion =
                pedidoRepository.calcularRecaudacionPorFestivalYUnidad(
                        idFestival, idUnidad);

        double costoPlatos =
                pedidoRepository.calcularCostoPlatosPorFestivalYUnidad(
                        idFestival, idUnidad);

        return calcularResultado(
                recaudacion, costoPlatos, festival, unidad);
    }

    @Override
    public double calcularRentabilidadNetaEntreFechas(
            Festival festival,
            UnidadDeVenta unidad,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        validarFestivalYUnidad(festival, unidad);

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException(
                    "Debés indicar la fecha inicial y la fecha final.");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final.");
        }

        Long idFestival = festival.getId();
        Long idUnidad = unidad.getId();

        double recaudacion =
                pedidoRepository.calcularRecaudacionPorFechas(
                        idFestival, idUnidad, fechaInicio, fechaFin);

        double costoPlatos =
                pedidoRepository.calcularCostoPlatosPorFechas(
                        idFestival, idUnidad, fechaInicio, fechaFin);

        // Los sueldos y el canon se descuentan completos,
        // aunque el intervalo incluya solo parte del festival.
        return calcularResultado(
                recaudacion, costoPlatos, festival, unidad);
    }

    private double calcularResultado(
            double recaudacion,
            double costoPlatos,
            Festival festival,
            UnidadDeVenta unidad) {

        double sueldos = empleadoRepository
                .findByUnidadAsignada_Id(unidad.getId())
                .stream()
                .mapToDouble(empleado -> empleado.calcularSueldo(festival))
                .sum();

        double canon = unidad.calcularCanon();

        return recaudacion - costoPlatos - sueldos - canon;
    }

    private void validarFestivalYUnidad(
            Festival festival,
            UnidadDeVenta unidad) {

        if (festival == null || festival.getId() == null) {
            throw new IllegalArgumentException(
                    "Debés indicar un festival existente.");
        }

        if (unidad == null || unidad.getId() == null) {
            throw new IllegalArgumentException(
                    "Debés indicar una unidad de venta existente.");
        }

        boolean perteneceAlFestival = festival.getLstUdv()
                .stream()
                .anyMatch(u -> u.getId() != null
                        && u.getId().equals(unidad.getId()));

        if (!perteneceAlFestival) {
            throw new IllegalArgumentException(
                    "La unidad de venta no pertenece al festival indicado.");
        }
    }
}
