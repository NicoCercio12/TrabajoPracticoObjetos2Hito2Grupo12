package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IEmpleadoService;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/responsable")
public class ResponsableController {

    private final IUnidadDeVentaService unidadDeVentaService;
    private final IEmpleadoService empleadoService;
    private final IUnidadDeVentaRepository unidadRepository;
    private final IFestivalRepository festivalRepository;

    public ResponsableController(IUnidadDeVentaService unidadDeVentaService,
                                 IEmpleadoService empleadoService,
                                 IUnidadDeVentaRepository unidadRepository,
                                 IFestivalRepository festivalRepository) {
        this.unidadDeVentaService = unidadDeVentaService;
        this.empleadoService = empleadoService;
        this.unidadRepository = unidadRepository;
        this.festivalRepository = festivalRepository;
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
        Empleado empleado = empleadoService.buscarPorId(idEmpleado);
        Festival festival = festivalRepository.findById(idFestival)
                .orElseThrow(() -> new IllegalArgumentException("Festival no encontrado con ID: " + idFestival));

        model.addAttribute("empleado", empleado);
        model.addAttribute("festival", festival);
        model.addAttribute("sueldo", sueldo);

        return "responsable/liquidacion";
    }

    @PostMapping("/empleados/{idEmpleado}/baja")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public String darDeBajaEmpleado(
            @PathVariable Long idEmpleado,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            empleadoService.darDeBaja(idEmpleado, principal.getName());

            redirectAttributes.addFlashAttribute(
                    "mensaje", "Empleado dado de baja correctamente");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/responsable/empleados";
    }

    @PostMapping("/empleados/{idEmpleado}/reactivar")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public String reactivarEmpleado(
            @PathVariable Long idEmpleado,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        empleadoService.reactivar(idEmpleado, principal.getName());

        redirectAttributes.addFlashAttribute(
                "mensaje", "Empleado reactivado correctamente");

        return "redirect:/responsable/empleados";
    }

    @GetMapping("/empleados")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public String listarEmpleados(
            @RequestParam(defaultValue = "true") boolean activo,
            Principal principal,
            Model model) {

        model.addAttribute(
                "empleados",
                empleadoService.listarPorResponsable(
                        principal.getName(), activo));

        model.addAttribute("activo", activo);

        Authentication authentication = (Authentication) principal;

        java.util.List<String> roles = new java.util.ArrayList<>();

        for (var autoridad : authentication.getAuthorities()) {
            roles.add(autoridad.getAuthority());
        }

        model.addAttribute("logueado", true);
        model.addAttribute("username", principal.getName());
        model.addAttribute("roles", roles);

        return "responsable/empleados";
    }

    @GetMapping("/unidades")
    public String misUnidades(Principal principal, Model model) {
        model.addAttribute("unidades",
                unidadRepository.findByResponsable_Usuario_Username(principal.getName()));
        return "responsable/unidades";
    }
}