package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.dtos.PlatoMasVendidoDTO;
import com.grupo12.epicentro_gourmet.entities.Pedido;
import com.grupo12.epicentro_gourmet.entities.Plato;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    // Top 3 Platos Más Vendidos por Unidad y Festival
    @Query("SELECT new com.grupo12.epicentro_gourmet.dtos.PlatoMasVendidoDTO(" +
           "item.plato.nombre, " +
           "SUM(item.cantidad), " +
           "SUM(item.cantidad * item.plato.precioVenta)) " +
           "FROM Pedido p JOIN p.lstItems item " +
           "WHERE p.festival.id = :idFestival AND p.unidadDeVenta.id = :idUnidad " +
           "GROUP BY item.plato.id, item.plato.nombre, item.plato.precioVenta " +
           "ORDER BY SUM(item.cantidad) DESC")
    List<PlatoMasVendidoDTO> findTopPlatosByUnidadAndFestival(
            @Param("idFestival") Long idFestival,
            @Param("idUnidad") Long idUnidad,
            Pageable pageable
    );

    // Rentabilidad: recaudación total por festival y unidad
    @Query("SELECT COALESCE(SUM(item.cantidad * item.plato.precioVenta), 0) " +
           "FROM Pedido p JOIN p.lstItems item " +
           "WHERE p.festival.id = :festivalId " +
           "AND p.unidadDeVenta.id = :unidadId")
    Double calcularRecaudacionPorFestivalYUnidad(
            @Param("festivalId") Long festivalId,
            @Param("unidadId") Long unidadId);

    // Rentabilidad: costo de producción de platos por festival y unidad
    @Query("SELECT COALESCE(SUM(item.cantidad * item.plato.costoProduccion), 0) " +
           "FROM Pedido p JOIN p.lstItems item " +
           "WHERE p.festival.id = :festivalId " +
           "AND p.unidadDeVenta.id = :unidadId")
    Double calcularCostoPlatosPorFestivalYUnidad(
            @Param("festivalId") Long festivalId,
            @Param("unidadId") Long unidadId);

    // Rentabilidad entre fechas: recaudación
    @Query("SELECT COALESCE(SUM(item.cantidad * item.plato.precioVenta), 0) " +
           "FROM Pedido p JOIN p.lstItems item " +
           "WHERE p.festival.id = :festivalId " +
           "AND p.unidadDeVenta.id = :unidadId " +
           "AND p.fecha BETWEEN :fechaInicio AND :fechaFin")
    Double calcularRecaudacionPorFechas(
            @Param("festivalId") Long festivalId,
            @Param("unidadId") Long unidadId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    // Rentabilidad entre fechas: costo de producción de platos
    @Query("SELECT COALESCE(SUM(item.cantidad * item.plato.costoProduccion), 0) " +
           "FROM Pedido p JOIN p.lstItems item " +
           "WHERE p.festival.id = :festivalId " +
           "AND p.unidadDeVenta.id = :unidadId " +
           "AND p.fecha BETWEEN :fechaInicio AND :fechaFin")
    Double calcularCostoPlatosPorFechas(
            @Param("festivalId") Long festivalId,
            @Param("unidadId") Long unidadId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}