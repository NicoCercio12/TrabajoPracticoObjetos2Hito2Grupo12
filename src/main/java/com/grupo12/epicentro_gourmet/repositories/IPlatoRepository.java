package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grupo12.epicentro_gourmet.dtos.PlatoListadoDTO;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface IPlatoRepository extends JpaRepository<Plato, Long> {
    @Query("""
        SELECT new com.grupo12.epicentro_gourmet.dtos.PlatoListadoDTO(
            p.idPlato,
            p.nombre,
            p.precioVenta,
            p.costoProduccion,
            u.id,
            u.nombreComercial
        )
        FROM UnidadDeVenta u
        JOIN u.lstPlatos p
        WHERE u.responsable.usuario.username = :username
        ORDER BY u.nombreComercial, p.nombre, p.idPlato
        """)
    List<PlatoListadoDTO> listarPorResponsable(
            @Param("username") String username);
}
