package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IEmpleadoService;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import com.grupo12.epicentro_gourmet.services.IReporteMasVendidoService;
import com.grupo12.epicentro_gourmet.dtos.PlatoMasVendidoDTO;
import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import com.grupo12.epicentro_gourmet.dtos.AltaEmpleadoDTO;
import com.grupo12.epicentro_gourmet.enums.TipoEmpleado;
import com.grupo12.epicentro_gourmet.enums.Turno;
import com.grupo12.epicentro_gourmet.enums.RolCocina;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.mail.MailException;

@Controller
@RequestMapping("/responsable")
public class ResponsableController {

    private final IUnidadDeVentaService unidadDeVentaService;
    private final IEmpleadoService empleadoService;
    private final IUnidadDeVentaRepository unidadRepository;
    private final IFestivalRepository festivalRepository;
    private final IReporteMasVendidoService reporteMasVendidoService;

    public ResponsableController(IUnidadDeVentaService unidadDeVentaService,
                                 IEmpleadoService empleadoService,
                                 IUnidadDeVentaRepository unidadRepository,
                                 IFestivalRepository festivalRepository,
                                 IReporteMasVendidoService reporteMasVendidoService) {
        this.unidadDeVentaService = unidadDeVentaService;
        this.empleadoService = empleadoService;
        this.unidadRepository = unidadRepository;
        this.festivalRepository = festivalRepository;
        this.reporteMasVendidoService = reporteMasVendidoService;
    }

    @GetMapping({"", "/"})
    public String dashboard(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        return "responsable/dashboard";
    }

    // Endpoint para consultar el Top 3 Platos más vendidos de la Unidad
    @GetMapping("/unidades/{idUnidad}/top-platos")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public String verTopPlatosUnidad(
            @PathVariable Long idUnidad,
            @RequestParam(value = "idFestival", defaultValue = "1") Long idFestival,
            Authentication authentication,
            Model model) {

        UnidadDeVenta unidad = unidadRepository.findById(idUnidad)
                .orElseThrow(() -> new IllegalArgumentException("Unidad no encontrada con ID: " + idUnidad));

        // Seguridad: verificar que la unidad pertenezca al Responsable en sesión
        if (!unidad.getResponsable().getUsuario().getUsername().equals(authentication.getName())) {
            return "redirect:/responsable/unidades?error=acceso_denegado";
        }

        Festival festival = festivalRepository.findById(idFestival)
                .orElseThrow(() -> new IllegalArgumentException("Festival no encontrado con ID: " + idFestival));

        List<PlatoMasVendidoDTO> topPlatos = reporteMasVendidoService.obtenerTop3PlatosPorUnidadYFestival(idFestival, idUnidad);

        model.addAttribute("unidad", unidad);
        model.addAttribute("festival", festival);
        model.addAttribute("topPlatos", topPlatos);

        // Atributos de sesión
        java.util.List<String> roles = new java.util.ArrayList<>();
        for (var autoridad : authentication.getAuthorities()) {
            roles.add(autoridad.getAuthority());
        }
        model.addAttribute("logueado", true);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", roles);

        return "responsable/top_platos";
    }

    // Endpoint para consultar el Canon a pagar por la Unidad de Venta
    @GetMapping("/canon/{idUnidad}")
    public String verCanon(@PathVariable Long idUnidad, Model model) {
        double canon = unidadDeVentaService.calcularCanon(idUnidad);
        model.addAttribute("unidad", unidadDeVentaService.buscarPorId(idUnidad));
        model.addAttribute("canon", canon);
        return "responsable/canon";
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
            redirectAttributes.addFlashAttribute("mensaje", "Empleado dado de baja correctamente");
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
        redirectAttributes.addFlashAttribute("mensaje", "Empleado reactivado correctamente");

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
                empleadoService.listarPorResponsable(principal.getName(), activo));

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
        model.addAttribute("unidades", unidadRepository.findByResponsable_Usuario_Username(principal.getName()));
        return "responsable/unidades";
    }

    @GetMapping("/empleados/nuevo")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public String formularioAltaEmpleado(Authentication authentication, Model model) {
        model.addAttribute("empleadoDTO", new AltaEmpleadoDTO());
        cargarDatosFormularioEmpleado(authentication, model);
        return "responsable/empleado-formulario";
    }

    @PostMapping("/empleados/nuevo")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public String registrarEmpleado(@Valid @ModelAttribute("empleadoDTO") AltaEmpleadoDTO dto, 
                                    BindingResult result, 
                                    Authentication authentication, 
                                    Model model, 
                                    RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            cargarDatosFormularioEmpleado(authentication, model);
            return "responsable/empleado-formulario";
        }

        try {
            empleadoService.darDeAlta(dto, authentication.getName());
        } catch (IllegalArgumentException e) {
            result.reject("alta.invalida", e.getMessage());
            cargarDatosFormularioEmpleado(authentication, model);
            return "responsable/empleado-formulario";
        } catch (MailException e) {
            result.reject("correo.error", "No se pudo enviar el correo. El alta no se completó. Revisá la configuración de correo e intentá nuevamente.");
            cargarDatosFormularioEmpleado(authentication, model);
            return "responsable/empleado-formulario";
        }

        redirectAttributes.addFlashAttribute("mensaje", "Empleado registrado. Se enviaron sus datos de acceso por correo.");
        return "redirect:/responsable/empleados";
    }

    private void cargarDatosFormularioEmpleado(Authentication authentication, Model model) {
        model.addAttribute(
                "unidades",
                unidadRepository.findByResponsable_Usuario_Username(authentication.getName()));

        model.addAttribute("tiposEmpleado", TipoEmpleado.values());
        model.addAttribute("turnos", Turno.values());
        model.addAttribute("rolesCocina", RolCocina.values());

        java.util.List<String> roles = new java.util.ArrayList<>();
        for (var autoridad : authentication.getAuthorities()) {
            roles.add(autoridad.getAuthority());
        }

        model.addAttribute("logueado", true);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", roles);
    }

    @GetMapping("/platos/vista")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public String verPlatos(Authentication authentication, Model model) {
        model.addAttribute("unidades", unidadRepository.findByResponsable_Usuario_Username(authentication.getName()));

        java.util.List<String> roles = new java.util.ArrayList<>();
        for (var autoridad : authentication.getAuthorities()) {
            roles.add(autoridad.getAuthority());
        }

        model.addAttribute("logueado", true);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", roles);

        return "responsable/platos";
    }
}