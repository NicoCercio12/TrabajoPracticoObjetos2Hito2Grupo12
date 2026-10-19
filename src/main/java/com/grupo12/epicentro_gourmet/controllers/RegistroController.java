package com.grupo12.epicentro_gourmet.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.grupo12.epicentro_gourmet.dtos.RegistroUnidadDTO;
import com.grupo12.epicentro_gourmet.enums.RolCocina;
import com.grupo12.epicentro_gourmet.enums.TipoEmpleado;
import com.grupo12.epicentro_gourmet.enums.TipoUnidad;
import com.grupo12.epicentro_gourmet.enums.Turno;
import com.grupo12.epicentro_gourmet.helpers.ViewRouteHelper;
import com.grupo12.epicentro_gourmet.services.IRegistroService;
import jakarta.validation.Valid;

import java.security.Principal;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    private final IRegistroService registroService;

    public RegistroController(IRegistroService registroService) {
        this.registroService = registroService;
    }

    @ModelAttribute("tiposUnidad")
    public TipoUnidad[] tiposUnidad() { return TipoUnidad.values(); }

    @ModelAttribute("tiposEmpleado")
    public TipoEmpleado[] tiposEmpleado() { return TipoEmpleado.values(); }

    @ModelAttribute("rolesCocina")
    public RolCocina[] rolesCocina() { return RolCocina.values(); }

    @ModelAttribute("turnos")
    public Turno[] turnos() { return Turno.values(); }

    @GetMapping
    public String formulario(Model model) {
        model.addAttribute("registro", new RegistroUnidadDTO());
        return ViewRouteHelper.REGISTRO;
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("registro") RegistroUnidadDTO dto,
                            BindingResult result, Principal principal, HttpServletRequest request) {
        if (result.hasErrors()) return ViewRouteHelper.REGISTRO;
        try {
            registroService.registrarUnidadConResponsable(dto, principal.getName());
        } catch (IllegalArgumentException e) {
            result.reject("registro.invalido", e.getMessage());
            return ViewRouteHelper.REGISTRO;
        }
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/login?registrada";
    }
}