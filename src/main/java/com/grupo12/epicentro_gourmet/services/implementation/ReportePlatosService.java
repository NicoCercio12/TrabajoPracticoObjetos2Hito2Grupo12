package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.PlatoVendidoDTO;
import com.grupo12.epicentro_gourmet.repositories.IPedidoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IReportePlatosService;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportePlatosService implements IReportePlatosService {

    private final IPedidoRepository pedidoRepository;
    private final IUnidadDeVentaRepository unidadRepository;

    public ReportePlatosService(
            IPedidoRepository pedidoRepository,
            IUnidadDeVentaRepository unidadRepository) {

        this.pedidoRepository = pedidoRepository;
        this.unidadRepository = unidadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlatoVendidoDTO> traerPlatosVendidosEntreFechas(
            Long unidadId,
            LocalDate desde,
            LocalDate hasta,
            String usernameResponsable) {

        if (usernameResponsable == null
                || usernameResponsable.isBlank()) {

            throw new AccessDeniedException(
                    "No se identificó al responsable");
        }

        if (unidadId == null || unidadId <= 0) {
            throw new IllegalArgumentException(
                    "Seleccioná una unidad de venta válida");
        }

        if (desde == null || hasta == null) {
            throw new IllegalArgumentException(
                    "Completá las fechas desde y hasta");
        }

        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "La fecha desde no puede ser posterior a la fecha hasta");
        }

        boolean esResponsable = unidadRepository
                .existsByIdAndResponsable_Usuario_Username(
                        unidadId, usernameResponsable);

        if (!esResponsable) {
            throw new AccessDeniedException(
                    "No podés consultar ventas de una unidad ajena");
        }

        return pedidoRepository.traerPlatosVendidosEntreFechas(
                unidadId,
                desde,
                hasta,
                usernameResponsable);
    }
}