package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IReportePlatosService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/responsable/platos-vendidos")
@PreAuthorize("hasRole('RESPONSABLE')")
public class ReportePlatosController {

    private final IReportePlatosService reporteService;
    private final IUnidadDeVentaRepository unidadRepository;

    public ReportePlatosController(
            IReportePlatosService reporteService,
            IUnidadDeVentaRepository unidadRepository) {

        this.reporteService = reporteService;
        this.unidadRepository = unidadRepository;
    }

    @GetMapping
    public String verReporte(
            @RequestParam(required = false) Long unidadId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "false") boolean consultar,
            Authentication authentication,
            Model model) {

        model.addAttribute(
                "unidades",
                unidadRepository.findByResponsable_Usuario_Username(
                        authentication.getName()));

        model.addAttribute("username", authentication.getName());
        model.addAttribute("unidadId", unidadId);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("consultado", false);

        if (consultar) {
            try {
                model.addAttribute(
                        "platos",
                        reporteService.traerPlatosVendidosEntreFechas(unidadId, desde, hasta, authentication.getName()));

                model.addAttribute("consultado", true);

            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        return "responsable/platos-vendidos";
    }
}