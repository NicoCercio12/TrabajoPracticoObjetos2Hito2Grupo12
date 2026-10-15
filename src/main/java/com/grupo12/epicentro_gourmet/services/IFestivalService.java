package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Festival;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface IFestivalService {

    List<Festival> getAll();
    Optional<Festival> findById(Long id);
    Festival insertOrUpdate(Festival festival);
    boolean remove(Long id);
    List<Festival> findActivos();

}
