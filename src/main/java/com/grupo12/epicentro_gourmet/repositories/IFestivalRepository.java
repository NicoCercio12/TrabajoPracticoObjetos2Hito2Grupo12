package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFestivalRepository extends JpaRepository<Festival, Long> {
}
