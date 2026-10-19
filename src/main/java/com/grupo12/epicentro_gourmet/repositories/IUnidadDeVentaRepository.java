package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUnidadDeVentaRepository extends JpaRepository<UnidadDeVenta, Long> {

    boolean existsByCodigoUnico(String codigoUnico);

    boolean existsByIdAndResponsable_Usuario_Username(Long idUnidad, String username);

}
