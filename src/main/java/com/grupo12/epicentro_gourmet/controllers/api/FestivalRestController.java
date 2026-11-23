package com.grupo12.epicentro_gourmet.controllers.api;

// DTO´S DE LOS CONSULTAS Y RESPUESTAS

import com.grupo12.epicentro_gourmet.dtos.FestivalRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.FestivalResponseDTO;


import com.grupo12.epicentro_gourmet.services.IFestivalService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/festivales")
@RequiredArgsConstructor
@Tag(name = "Festivales", description = "Endpoints REST para la gestión y consulta de festivales temáticos")
public class FestivalRestController {
    


private final IFestivalService festivalService; // Ajustá al nombre de tu interfaz o clase Service

    // 1. ENDPOINT POST: Crear un festival
    @Operation(
        summary = "Crear un nuevo festival",
        description = "Registra un festival temático con su nombre, temporada y fechas de inicio y fin."
    )
    @ApiResponse(responseCode = "201", description = "Festival creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<FestivalResponseDTO> crearFestival(
            @Valid @RequestBody FestivalRequestDTO requestDTO) {
        
        FestivalResponseDTO creado = festivalService.crearFestival(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);

    }


// GET : BUSCAR POR 3 PARAMETROS 
    @Operation(
        summary = "Buscar festivales con filtros",
        description = "Permite filtrar festivales de forma opcional por nombre, temporada y fecha de iniicio."
    )
    @ApiResponse(responseCode = "200", description = "festival obtenido correctamente")
    @ApiResponse(responseCode = "404", description = "festival no encontrado")
    @GetMapping
    public ResponseEntity <FestivalResponseDTO> buscarFestival(
            @Parameter(description = "Nombre del festival", example = "FESTIVAL DE LA HAMBURGUESA")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Temporada del festival", example = "Otoño")
            @RequestParam(required = false) String temporada,

            @Parameter(description = "Fecha de inicio del festival", example = "2026-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde) {

        FestivalResponseDTO resultado = festivalService.obternerPorNombreTemporadaFecha(nombre, temporada, fechaDesde);
        return ResponseEntity.ok(resultado);
    }



}
