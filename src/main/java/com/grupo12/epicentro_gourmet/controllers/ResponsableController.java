package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.services.IEmpleadoService;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/responsable")
public class ResponsableController {

    private final IUnidadDeVentaService unidadDeVentaService;
    private final IEmpleadoService empleadoService;

    public ResponsableController(IUnidadDeVentaService unidadDeVentaService, IEmpleadoService empleadoService) {
        this.unidadDeVentaService = unidadDeVentaService;
        this.empleadoService = empleadoService;
    }

    // Endpoint para consultar el Canon a pagar por la Unidad de Venta
    @GetMapping("/canon/{idUnidad}")
    public String verCanon(@PathVariable Long idUnidad, Model model) {
        double canon = unidadDeVentaService.calcularCanon(idUnidad);
        model.addAttribute("unidad", unidadDeVentaService.buscarPorId(idUnidad));
        model.addAttribute("canon", canon);
        return "responsable/canon"; // Vista HTML en templates/responsable/canon.html
    }

    // Endpoint para consultar la Liquidación de Haberes de un Empleado en un Festival
    @GetMapping("/liquidacion/empleado/{idEmpleado}/festival/{idFestival}")
    public String verLiquidacion(@PathVariable Long idEmpleado,
                                 @PathVariable Long idFestival,
                                 Model model) {
        double sueldo = empleadoService.calcularLiquidacionHaber(idEmpleado, idFestival);
        model.addAttribute("empleado", empleadoService.buscarPorId(idEmpleado));
        model.addAttribute("sueldo", sueldo);
        return "responsable/liquidacion"; // Vista HTML en templates/responsable/liquidacion.html
    }
}