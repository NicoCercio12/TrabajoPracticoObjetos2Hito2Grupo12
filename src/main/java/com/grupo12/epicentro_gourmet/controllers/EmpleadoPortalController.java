package com.grupo12.epicentro_gourmet.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.services.IEmpleadoService;
import com.grupo12.epicentro_gourmet.services.implementation.ReciboSueldoService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoPortalController {

    private final IEmpleadoService empleadoService;
    private final ReciboSueldoService reciboService;

    public EmpleadoPortalController(IEmpleadoService empleadoService, ReciboSueldoService reciboService) {
        this.empleadoService = empleadoService;
        this.reciboService = reciboService;
    }

    @GetMapping("/mi-perfil")
    public String miPerfil(Principal principal, Model model) {
        try {
            Empleado empleado = empleadoService.buscarPorUsername(principal.getName());
            var festivales = empleadoService.festivalesDelEmpleado(empleado);
            LocalDate hoy = LocalDate.now();

            Map<Long, Double> sueldos = new HashMap<>();
            for (Festival f : festivales) {
                if (!hoy.isBefore(f.getFechaInicio())) {
                    sueldos.put(f.getId(), empleado.calcularSueldo(f));
                }
            }

            Festival enCurso = festivales.stream()
                    .filter(f -> !hoy.isBefore(f.getFechaInicio()) && !hoy.isAfter(f.getFechaFin()))
                    .findFirst().orElse(null);
            double totalSueldos = sueldos.values().stream().mapToDouble(Double::doubleValue).sum();

            model.addAttribute("festivalEnCurso", enCurso);
            model.addAttribute("totalSueldos", totalSueldos);

            model.addAttribute("empleado", empleado);
            model.addAttribute("festivales", festivales);
            model.addAttribute("sueldos", sueldos);
            model.addAttribute("hoy", hoy);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "empleado/mi-perfil";
    }

    @GetMapping("/recibo/{idFestival}")
    public ResponseEntity<byte[]> recibo(@PathVariable Long idFestival, Principal principal) {
        Empleado empleado = empleadoService.buscarPorUsername(principal.getName());

        Festival festival = empleadoService.festivalesDelEmpleado(empleado).stream()
                .filter(f -> f.getId().equals(idFestival))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("No tenés un recibo para ese festival"));

        if (LocalDate.now().isBefore(festival.getFechaInicio())) {
            throw new AccessDeniedException("Ese festival todavía no empezó");
        }

        byte[] pdf = reciboService.generar(empleado, festival, empleado.calcularSueldo(festival));
        String archivo = "recibo-" + festival.getNombre().replaceAll("[^A-Za-z0-9]+", "-") + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(archivo).build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}