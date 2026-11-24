package com.grupo12.epicentro_gourmet.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull
    private Long festivalId;

    @NotNull
    private Long unidadId;

    private LocalDate fecha;

    private List<ItemPedidoRequest> items;

    @Data
    public static class ItemPedidoRequest {

        @NotNull
        private Long platoId;

        @NotNull
        @Positive
        private Integer cantidad;
    }
}