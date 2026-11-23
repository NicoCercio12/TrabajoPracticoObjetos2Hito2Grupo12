package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.grupo12.epicentro_gourmet.entities.Plato;
import java.util.Set;


@Repository
public interface IUnidadDeVentaRepository extends JpaRepository<UnidadDeVenta, Long> {

    List<UnidadDeVenta> findByResponsable_Usuario_Username(String username);

    boolean existsByCodigoUnico(String codigoUnico);

    boolean existsByIdAndResponsable_Usuario_Username(Long idUnidad, String username);

    // Busca unidades que pertenezcan al festival dado por su ID
    @Query("SELECT u FROM UnidadDeVenta u JOIN u.lstFestivales f WHERE f.id = :festivalId")
    List<UnidadDeVenta> findByFestivalesId(@Param("festivalId") Long festivalId);

    @Query("SELECT u FROM UnidadDeVenta u JOIN u.lstPlatos p WHERE u.codigoUnico= :codigoUnico AND LOWER(p.nombre) = LOWER(:nombrePlato)")
    UnidadDeVenta findByCodigoUnicoAndNombrePlato(
        @Param("codigoUnico") String codigoUnico,
        @Param("nombrePlato") String nombrePlato
);
    UnidadDeVenta findByCodigoUnico(String codigoUnico);
}
