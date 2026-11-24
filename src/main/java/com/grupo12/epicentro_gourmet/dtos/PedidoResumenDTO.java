package com.grupo12.epicentro_gourmet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResumenDTO {

    private Long idPedido;
    private LocalDate fecha;
    private Long festivalId;
    private Long unidadId;
    private int cantidadItems;
}