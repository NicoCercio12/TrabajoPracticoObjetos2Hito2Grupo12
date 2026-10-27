package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IFestivalRepository extends JpaRepository<Festival, Long> {

    List<Festival> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate hoy, LocalDate hoy1);
    List<Festival> findAllByOrderByFechaInicioAsc();
    List<Festival> findByLstUdvContainingOrderByFechaInicioDesc(UnidadDeVenta unidad);

}
