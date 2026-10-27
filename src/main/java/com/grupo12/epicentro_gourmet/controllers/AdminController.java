package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.implementation.FestivalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IEmpleadoRepository empleadoRepository;
    private final IUnidadDeVentaRepository unidadRepository;
    private final FestivalService festivalService;

    public AdminController(IEmpleadoRepository empleadoRepository, 
                           IUnidadDeVentaRepository unidadRepository,
                           FestivalService festivalService) {
        this.empleadoRepository = empleadoRepository;
        this.unidadRepository = unidadRepository;
        this.festivalService = festivalService;
    }

    // Ruta principal: http://localhost:8080/admin
    @GetMapping({"", "/"})
    public String dashboard(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        // Carga los festivales para la tabla del centro de comando
        model.addAttribute("festivales", festivalService.getAll());
        
        // Muestra src/main/resources/templates/admin/dashboard.html
        return "admin/dashboard";
    }

    @GetMapping("/empleados")
    public String empleados(@RequestParam(required = false) Boolean activo, Model model) {
        model.addAttribute("empleados",
                activo == null ? empleadoRepository.findAll() : empleadoRepository.findByActivo(activo));
        model.addAttribute("filtro", activo);

        return "admin/empleados";
    }

    @GetMapping("/unidades")
    public String unidades(Model model) {
        model.addAttribute("unidades", unidadRepository.findAll());
        return "admin/unidades";
    }
}