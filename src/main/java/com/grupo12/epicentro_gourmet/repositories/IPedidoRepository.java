package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
}
