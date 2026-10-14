package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlatoRepository extends JpaRepository<Plato, Long> {
}
