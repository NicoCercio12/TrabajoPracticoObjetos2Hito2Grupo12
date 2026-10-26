package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT SUM(item.cantidad * item.plato.precioVenta) FROM Pedido p JOIN p.lstItems item WHERE p.festival.id = :festivalId")
    Double calcularRecaudacionTotalPorFestival(@Param("festivalId") Long festivalId);
}