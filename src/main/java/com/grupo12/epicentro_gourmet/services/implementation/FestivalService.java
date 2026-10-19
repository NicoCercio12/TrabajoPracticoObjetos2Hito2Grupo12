package com.grupo12.epicentro_gourmet.services.implementation;
import com.grupo12.epicentro_gourmet.dtos.FestivalDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(readOnly = true)
    public List<FestivalDTO> obtenerTodosDTO() {
        // 1. Buscamos todos los festivales ordenados cronológicamente
        return repository.findAllByOrderByFechaInicioAsc()
                // 2. Abrimos un Stream para procesar la lista funcionalmente
                .stream()
                // 3. Mapeamos cada entidad Festival a su respectivo FestivalDTO llamando al método privado
                .map(this::mapearADTO)
                // 4. Agrupamos los DTOs resultantes en una nueva lista
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FestivalDTO obtenerPorIdDTO(Long id) {
        // Buscamos el festival, si no existe lanzamos una excepción descriptiva
        Festival festival = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festival no encontrado con ID: " + id));

        // Convertimos a DTO y lo devolvemos
        return mapearADTO(festival);
    }

    // Funcion auxiliar para mapear de la capa de entidad a la capa de dto
    private FestivalDTO mapearADTO(Festival festival) {
        // Calculamos la duración total en días (incluyendo el día de inicio y de fin)
        int duracion = 0;
        if (festival.getFechaInicio() != null && festival.getFechaFin() != null) {
            // ChronoUnit.DAYS.between calcula la diferencia; sumamos 1 para incluir el día de cierre
            duracion = (int) ChronoUnit.DAYS.between(festival.getFechaInicio(), festival.getFechaFin()) + 1;
        }

        // Verificamos si el festival está transcurriendo en la fecha actual
        LocalDate hoy = LocalDate.now();
        boolean estaActivo = false;
        if (festival.getFechaInicio() != null && festival.getFechaFin() != null) {
            estaActivo = (!hoy.isBefore(festival.getFechaInicio())) && (!hoy.isAfter(festival.getFechaFin()));
        }

        // Construimos la instancia de FestivalDTO con los valores calculados
        return FestivalDTO.builder()
                .id(festival.getId())
                .nombre(festival.getNombre())
                .temporada(festival.getTemporada())
                .fechaInicio(festival.getFechaInicio())
                .fechaFin(festival.getFechaFin())
                .duracionDias(duracion)
                .activo(estaActivo)
                .build();
    }
}