package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.services.ICierreCajaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/cajero")
public class CajeroController {

    private final ICierreCajaService service;

    public CajeroController(ICierreCajaService service) {
        this.service = service;
    }

    @GetMapping("/cierre")
    public String cierre(Principal principal, Model model) {
        try {

            model.addAttribute("total", service.totalDelDia(principal.getName()));
            model.addAttribute("yaCerro", service.yaCerroHoy(principal.getName()));
            model.addAttribute("historial", service.historial(principal.getName()));

        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "cajero/cierre";
    }

    @PostMapping("/cierre")
    public String cerrar(Principal principal, RedirectAttributes ra) {
        try {

            var c = service.cerrarCaja(principal.getName());
            ra.addFlashAttribute("mensaje", "Caja cerrada. Total Recaudado $" +c.getTotalRecaudado());

        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/cajero/cierre";
    }

}
