package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IFestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate hoy, LocalDate hoy1);
}
