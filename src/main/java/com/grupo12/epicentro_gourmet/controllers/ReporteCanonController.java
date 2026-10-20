
package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.dtos.ReporteMayoresCanonDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.services.IReporteCanonService;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/reportes/canones")
@RequiredArgsConstructor
public class ReporteCanonController {

    private final IReporteCanonService reporteCanonService;
    private final IFestivalService festivalService;

    @GetMapping
    public String mostrarReporte(
            @RequestParam Long idFestival,
            @RequestParam(defaultValue = "5") int cantidad,
            Model model) {

        Festival festival = festivalService.findById(idFestival)
                .orElseThrow(() ->
                        new IllegalArgumentException("Festival no encontrado"));

        List<ReporteMayoresCanonDTO> reporte =
                reporteCanonService.obtenerMayoresCanon(festival, cantidad);

        model.addAttribute("reporte", reporte);
        model.addAttribute("festival", festival);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("titulo", "Reporte de mayores cánones");

        return "reportes/mayores-canones";
    }
}
