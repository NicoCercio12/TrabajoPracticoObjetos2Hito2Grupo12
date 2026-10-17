package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.dtos.CuentaDTO;
import com.grupo12.epicentro_gourmet.services.implementation.CuentaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("signup")
    public String crear(@Valid @ModelAttribute("cuenta") CuentaDTO cuenta,
                        BindingResult br, RedirectAttributes ra) {

        if(cuenta.getPassword() != null
                && !cuenta.getPassword().equals(cuenta.getConfirmarPassword())) {

            br.rejectValue("confirmarPassword", "noCoincide", "Las contraseñas no coincidebn");

        }

        if(cuenta.getEmail() != null && !br.hasFieldErrors("email")
        && cuentaService.existe(cuenta.getEmail())) {

            br.rejectValue("email", "duplicado", "Ya existe una cuenta con este email");
        }

        if (br.hasErrors()) return "signup";

        cuentaService.crear(cuenta.getEmail(), cuenta.getPassword());
        ra.addFlashAttribute("mensaje",
                "Cuenta creada. Esperá a que un admin te asigne un rol");
        return "redirect:/login";
    }

    @GetMapping("/cuenta-pendiente")
    public String pendiente(){
        return "cuenta-pendiente";
    }
}
