package com.grupo12.epicentro_gourmet.controllers.api;

import com.grupo12.epicentro_gourmet.dtos.PedidoRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.PedidoResumenDTO;
import com.grupo12.epicentro_gourmet.services.IPedidoService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints REST para registrar, consultar, modificar y eliminar pedidos")
public class PedidoRestController {

    private final IPedidoService pedidoService;

    @Operation(
            summary = "Registrar un nuevo pedido",
            description = "Crea un pedido asociado a un festival, una unidad de venta y sus ítems de platos."
    )
    @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<PedidoResumenDTO> crearPedido(
            @Valid @RequestBody PedidoRequestDTO requestDTO) {

        PedidoResumenDTO creado = pedidoService.crearPedido(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
            summary = "Buscar pedidos por múltiples filtros",
            description = "Filtra pedidos por ID de festival, ID de unidad de venta, fecha desde y fecha hasta."
    )
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping
    public ResponseEntity<List<PedidoResumenDTO>> buscarPedidos(
            @Parameter(description = "ID del festival", example = "1")
            @RequestParam(required = false) Long festivalId,

            @Parameter(description = "ID de la unidad de venta", example = "1")
            @RequestParam(required = false) Long unidadId,

            @Parameter(description = "Fecha inicial del filtro", example = "2026-11-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,

            @Parameter(description = "Fecha final del filtro", example = "2026-11-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        List<PedidoResumenDTO> resultado = pedidoService.buscarPedidosConFiltros(
                festivalId, unidadId, fechaDesde, fechaHasta
        );
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Modificar un pedido existente",
            description = "Actualiza fecha, festival, unidad de venta o ítems de un pedido por su ID."
    )
    @ApiResponse(responseCode = "200", description = "Pedido actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PutMapping("/{idPedido}")
    public ResponseEntity<PedidoResumenDTO> actualizarPedido(
            @Parameter(description = "ID del pedido a modificar", example = "2")
            @PathVariable Long idPedido,
            @Valid @RequestBody PedidoRequestDTO requestDTO) {

        PedidoResumenDTO actualizado = pedidoService.actualizarPedido(idPedido, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(
            summary = "Eliminar un pedido",
            description = "Elimina un pedido existente y sus ítems asociados por su ID."
    )
    @ApiResponse(responseCode = "204", description = "Pedido eliminado correctamente")
    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> eliminarPedido(
            @Parameter(description = "ID del pedido a eliminar", example = "2")
            @PathVariable Long idPedido) {

        pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.noContent().build();
    }
}