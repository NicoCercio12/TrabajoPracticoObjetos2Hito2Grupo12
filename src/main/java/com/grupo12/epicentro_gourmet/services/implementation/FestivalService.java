package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FestivalService implements IFestivalService {

    private final IFestivalRepository repository;

    public FestivalService(IFestivalRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Festival> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Festival> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Festival insertOrUpdate(Festival festival) {
        return repository.save(festival);
    }

    @Override
    public boolean remove(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Festival> findActivos() {
        LocalDate hoy = LocalDate.now();
        return repository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(hoy, hoy);
    }
}
