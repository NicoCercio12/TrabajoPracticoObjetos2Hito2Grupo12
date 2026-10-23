package com.grupo12.epicentro_gourmet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grupo12.epicentro_gourmet.dtos.CuentaDTO;
import com.grupo12.epicentro_gourmet.services.implementation.CuentaService;

import jakarta.validation.Valid;

@Controller
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/signup")
    public String formulario(Model model) {
        model.addAttribute("cuenta", new CuentaDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String crear(@Valid @ModelAttribute("cuenta") CuentaDTO dto,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {

        if (dto.getPassword() != null && !dto.getPassword().equals(dto.getConfirmarPassword()))
            result.rejectValue("confirmarPassword", "no.coincide", "Las contraseñas no coinciden");

        if (dto.getEmail() != null && !result.hasFieldErrors("email") && cuentaService.existe(dto.getEmail()))
            result.rejectValue("email", "duplicado", "Ya existe una cuenta con ese email");

        if (result.hasErrors()) return "signup";

        cuentaService.crear(dto.getEmail(), dto.getPassword());

        redirectAttributes.addFlashAttribute("mensaje",
                "Cuenta creada. Un administrador tiene que aprobarla antes de que puedas registrar tu unidad.");
        return "redirect:/login";
    }

    @GetMapping("/cuenta-pendiente")
    public String pendiente() {
        return "cuenta-pendiente";
    }
}