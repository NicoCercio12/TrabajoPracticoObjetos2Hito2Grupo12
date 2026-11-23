package com.grupo12.epicentro_gourmet.controllers.api;

import com.grupo12.epicentro_gourmet.dtos.ConsultarAntiguedadEmpleadosResponseDTO;
import com.grupo12.epicentro_gourmet.dtos.ModificarPlatoDeUnidadRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.ModificarPlatoDeUnidadResponseDTO;
import com.grupo12.epicentro_gourmet.enums.TipoEstadoEmpleado;
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

@Operation(
    summary="Consultar los empleados con mas de x años de antiguedad",
    description="Permite consultar los  empleados  con una antiguedad superior al valor ingresado")
@ApiResponse (responseCode = "200", description ="Consulta realizada exitosamente" )
@ApiResponse(responseCode = "404", description = "Unidad no encontrada")
@GetMapping 
public ResponseEntity <ConsultarAntiguedadEmpleadosResponseDTO> consultarAntiguedadEmpleados(
    @Parameter(description = "Codigo Unico de la Unidad", example = "UDV-001")
    @RequestParam(required = false) String codigoUnico,

    @Parameter(description = "Parametro de antiguedad del cual se debe filtrar", example = "5")
    @RequestParam(required = false) int antiguedad,

    @Parameter(description = "Estado del Empleado, ACTIVO/INACTIVO/AMBOS", example = "ACTIVO")
    @RequestParam(required = false) TipoEstadoEmpleado estado){
    
    ConsultarAntiguedadEmpleadosResponseDTO consultaResponse=unidadDeVenta.consultarAntiguedadEmpleados(codigoUnico, antiguedad,estado);
    return ResponseEntity.ok(consultaResponse);
}

}
