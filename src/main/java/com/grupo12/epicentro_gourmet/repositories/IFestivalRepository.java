package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IFestivalRepository extends JpaRepository<Festival, Long> {
    //festivales activos
    List<Festival> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate hoy, LocalDate hoy1);
    // festivales ordenados ascendentes
    List<Festival> findAllByOrderByFechaInicioAsc();

}
