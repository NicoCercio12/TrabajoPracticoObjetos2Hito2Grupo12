package com.grupo12.epicentro_gourmet.controllers.api;

import com.grupo12.epicentro_gourmet.dtos.ModificarPlatoDeUnidadRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.ModificarPlatoDeUnidadResponseDTO;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/unidadDeVenta")
@RequiredArgsConstructor
@Tag(name = "UnidadesDeVenta", description = "Endpoints REST para la gestión y consulta de Unidades de Venta ")
public class UnidadDeVentaRestController {
private final IUnidadDeVentaService unidadDeVenta;
@Operation(
    summary="Modificar un plato de una  Unidad de Venta",
    description="Permite realizar una modificacion de una undiad de venta filtrando por su codigo unico y editando un plato de la lista"
)
@ApiResponse(responseCode = "201", description = "Plato de la unidad modificado  exitosamente")
@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
@ApiResponse(responseCode = "404", description = "Unidad o Plato no encontrado")
@PutMapping
public ResponseEntity <ModificarPlatoDeUnidadResponseDTO> modificarPlatoDeUnidad(@Valid @RequestBody ModificarPlatoDeUnidadRequestDTO requesDTO){


    ModificarPlatoDeUnidadResponseDTO unidadEditada = unidadDeVenta.modificarPlatoDeUnaUnidad(requesDTO.getCodigoUnico(),requesDTO.getNombrePlato(),requesDTO.getPrecioPlato());
    return ResponseEntity.status(HttpStatus.CREATED).body(unidadEditada);
}


}
