package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.dtos.ReporteVentaDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.services.implementation.ReporteRecaudacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reportes")
public class ReporteRecaudacionController {

    @Autowired
    private ReporteRecaudacionService reporteRecaudacionService;

    // Opción A: Accesible directo por ID de festival (como tus otros reportes)
    @GetMapping("/recaudacion/{id}")
    public String verReporteRecaudacionPorId(@PathVariable("id") Long idFestival, Model model) {
        Festival festival = reporteRecaudacionService.listarFestivales().stream()
                .filter(f -> f.getId().equals(idFestival))
                .findFirst()
                .orElse(null);

        ReporteVentaDTO reporte = reporteRecaudacionService.obtenerReporteRecaudacion(idFestival);

        model.addAttribute("festival", festival);
        model.addAttribute("reporte", reporte);
        return "reportes/recaudacion";
    }
}