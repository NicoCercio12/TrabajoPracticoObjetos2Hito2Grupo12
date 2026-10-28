
package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IRentabilidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class RentabilidadController {

    private final IRentabilidadService rentabilidadService;
    private final IFestivalRepository festivalRepository;
    private final IUnidadDeVentaRepository unidadRepository;

    public RentabilidadController(
            IRentabilidadService rentabilidadService,
            IFestivalRepository festivalRepository,
            IUnidadDeVentaRepository unidadRepository) {

        this.rentabilidadService = rentabilidadService;
        this.festivalRepository = festivalRepository;
        this.unidadRepository = unidadRepository;
    }

    @GetMapping("/reportes/rentabilidad")
    public String mostrarRentabilidad(
            @RequestParam Long idFestival,
            @RequestParam(required = false) Long idUnidad,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            Model model) {

        Festival festival = festivalRepository.findById(idFestival)
                .orElseThrow(() ->
                        new IllegalArgumentException("Festival no encontrado"));

        model.addAttribute("festival", festival);
        model.addAttribute("unidades", festival.getLstUdv());
        model.addAttribute("idUnidad", idUnidad);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("resultadoCalculado", false);

        // Si todavía no se seleccionó una unidad, mostramos el formulario.
        if (idUnidad == null) {
            return "reportes/rentabilidad";
        }

        try {
            UnidadDeVenta unidad = unidadRepository.findById(idUnidad)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Unidad de venta no encontrada"));

            double rentabilidad;

            // Si se indicó alguna fecha, deben estar indicadas ambas.
            if (fechaInicio != null || fechaFin != null) {

                if (fechaInicio == null || fechaFin == null) {
                    throw new IllegalArgumentException(
                            "Debés indicar ambas fechas para calcular el período.");
                }

                rentabilidad =
                        rentabilidadService.calcularRentabilidadNetaEntreFechas(
                                festival, unidad, fechaInicio, fechaFin);

                model.addAttribute("esPorFechas", true);

            } else {
                rentabilidad =
                        rentabilidadService.calcularRentabilidadNeta(
                                festival, unidad);

                model.addAttribute("esPorFechas", false);
            }

            model.addAttribute("unidad", unidad);
            model.addAttribute("rentabilidad", rentabilidad);
            model.addAttribute("resultadoCalculado", true);

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "reportes/rentabilidad";
    }
}
