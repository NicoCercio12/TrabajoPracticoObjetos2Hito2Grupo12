package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.dtos.AltaPlatoDTO;
import com.grupo12.epicentro_gourmet.dtos.PlatoListadoDTO;
import com.grupo12.epicentro_gourmet.entities.Plato;
import com.grupo12.epicentro_gourmet.services.IPlatoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/responsable/platos")
@PreAuthorize("hasRole('RESPONSABLE')")
public class PlatoController {
    private final IPlatoService platoService;

    public PlatoController(IPlatoService platoService) {
        this.platoService = platoService;
    }

    @PostMapping
    public ResponseEntity<?> registrarPlato(
            @Valid @RequestBody AltaPlatoDTO dto,
            BindingResult result,
            Authentication authentication) {

        if (result.hasErrors()) {
            List<String> errores = result.getAllErrors().stream().map(error -> error.getDefaultMessage()).toList();

            return ResponseEntity.badRequest().body(Map.of("errores", errores));
        }

        try {
            Plato plato = platoService.darDeAlta(dto.getNombre(), dto.getPrecioVenta(), dto.getCostoProduccion(), dto.getIdUnidad(), authentication.getName());

            Map<String, Object> respuesta = new LinkedHashMap<>();

            respuesta.put("mensaje", "Plato registrado correctamente");
            respuesta.put("idPlato", plato.getIdPlato());
            respuesta.put("nombre", plato.getNombre());
            respuesta.put("precioVenta", plato.getPrecioVenta());
            respuesta.put("costoProduccion", plato.getCostoProduccion());
            respuesta.put("idUnidad", dto.getIdUnidad());

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("errores", List.of(e.getMessage())));
        }
    }

    @GetMapping
    public ResponseEntity<List<PlatoListadoDTO>> listarPlatos(Authentication authentication) {

        return ResponseEntity.ok(platoService.listarPorResponsable(authentication.getName()));
    }

}