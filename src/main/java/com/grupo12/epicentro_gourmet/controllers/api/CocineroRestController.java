package com.grupo12.epicentro_gourmet.controllers.api;

import com.grupo12.epicentro_gourmet.dtos.CocineroRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.CocineroResumenDTO;
import com.grupo12.epicentro_gourmet.enums.RolCocina;
import com.grupo12.epicentro_gourmet.services.ICocineroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocineros")
@RequiredArgsConstructor
@Tag(name = "Cocineros", description = "Endpoints REST para registrar y consultar cocineros")
public class CocineroRestController {

    private final ICocineroService cocineroService;

    @Operation(
            summary = "Registrar un nuevo cocinero",
            description = "Crea un cocinero validando DNI único y mayoría de edad, asigna unidad de venta y calcula sueldo si se indica un festival."
    )
    @ApiResponse(responseCode = "201", description = "Cocinero creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos, DNI duplicado o menor de edad")
    @PostMapping
    public ResponseEntity<CocineroResumenDTO> crearCocinero(
            @Valid @RequestBody CocineroRequestDTO requestDTO) {

        CocineroResumenDTO creado = cocineroService.crearCocinero(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
            summary = "Buscar cocineros con 3 filtros principales",
            description = "Filtra cocineros por unidad de venta, rol de cocina y estado activo, con cálculo opcional de sueldo por festival."
    )
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping
    public ResponseEntity<List<CocineroResumenDTO>> buscarCocineros(
            @Parameter(description = "ID de la unidad de venta", example = "1")
            @RequestParam(required = false) Long unidadId,

            @Parameter(description = "Rol en cocina", example = "COCINERO")
            @RequestParam(required = false) RolCocina rolCocina,

            @Parameter(description = "Estado activo del empleado", example = "true")
            @RequestParam(required = false) Boolean activo,

            @Parameter(description = "ID de festival opcional para calcular sueldo estimado", example = "2")
            @RequestParam(required = false) Long festivalId) {

        List<CocineroResumenDTO> resultado = cocineroService.buscarCocineros(
                unidadId, rolCocina, activo, festivalId
        );
        return ResponseEntity.ok(resultado);
    }
}