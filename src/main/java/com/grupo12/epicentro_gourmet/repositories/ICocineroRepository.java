package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Cocinero;
import com.grupo12.epicentro_gourmet.enums.RolCocina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICocineroRepository extends JpaRepository<Cocinero, Long> {

    @Query("""
        SELECT c
        FROM Cocinero c
        LEFT JOIN c.unidadAsignada u
        WHERE (:unidadId IS NULL OR u.id = :unidadId)
          AND (:rolCocina IS NULL OR c.rolCocina = :rolCocina)
          AND (:activo IS NULL OR c.activo = :activo)
        ORDER BY c.apellido, c.nombre
        """)
    List<Cocinero> buscarConFiltros(
            @Param("unidadId") Long unidadId,
            @Param("rolCocina") RolCocina rolCocina,
            @Param("activo") Boolean activo
    );
}