package com.grupo12.epicentro_gourmet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plato plato;

    private int cantidad;

    public ItemPedido(Pedido pedido, Plato plato, int cantidad) {
        this.pedido = pedido;
        this.plato = plato;
        this.cantidad = cantidad;
    }
}