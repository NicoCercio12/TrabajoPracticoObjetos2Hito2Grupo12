package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
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

    public AdminController(IEmpleadoRepository empleadoRepository, IUnidadDeVentaRepository unidadRepository) {
        this.empleadoRepository = empleadoRepository;
        this.unidadRepository = unidadRepository;
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
