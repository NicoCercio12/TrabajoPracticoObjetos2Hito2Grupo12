package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Pedido;
import com.grupo12.epicentro_gourmet.entities.Plato;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT SUM(item.cantidad * item.plato.precioVenta) FROM Pedido p JOIN p.lstItems item WHERE p.festival.id = :festivalId")
    Double calcularRecaudacionTotalPorFestival(@Param("festivalId") Long festivalId);

    // Consulta para obtener el plato estrella por festival y unidad de venta
    @Query("SELECT item.plato FROM Pedido p JOIN p.lstItems item " +
            "WHERE p.festival.id = :festivalId AND p.unidadDeVenta.id = :unidadId " +
            "GROUP BY item.plato " +
            "ORDER BY SUM(item.cantidad) DESC")
    List<Plato> encontrarPlatoEstrella(@Param("festivalId") Long festivalId,
                                       @Param("unidadId") Long unidadId,
                                       Pageable pageable);
}