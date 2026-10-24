package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final IEmpleadoRepository empleadoRepository;
    private final IFestivalRepository festivalRepository;

    public EmpleadoController(IEmpleadoRepository empleadoRepository, IFestivalRepository festivalRepository) {
        this.empleadoRepository = empleadoRepository;
        this.festivalRepository = festivalRepository;
    }

    @GetMapping("/{id}/detalle")
    public String verDetalleEmpleado(@PathVariable Long id, Model model) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        // Buscamos el primer festival activo para hacer el cálculo de haberes
        Festival festivalActivo = festivalRepository.findAll().stream()
                .filter(Festival::isActivo)
                .findFirst()
                .orElse(null);

        double sueldoCalculado = 0.0;
        if (festivalActivo != null) {
            sueldoCalculado = empleado.calcularSueldo(festivalActivo);
        }

        model.addAttribute("empleado", empleado);
        model.addAttribute("festival", festivalActivo);
        model.addAttribute("sueldoCalculado", sueldoCalculado);

        return "empleados/detalle";
    }
}