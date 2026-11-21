package com.grupo12.epicentro_gourmet.services.implementation;
import com.grupo12.epicentro_gourmet.dtos.FestivalDTO;
import com.grupo12.epicentro_gourmet.dtos.FestivalRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.FestivalResponseDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.ObjectUtils.Null;
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

    //Funcion local de este dominio para calucalar la duracion en una unidad entera 

    private int CalcularDuracion(LocalDate FechaIncio,LocalDate FechaFin){

        int duracion = 0;
        if (FechaIncio != null && FechaFin != null) {
            // ChronoUnit.DAYS.between calcula la diferencia; sumamos 1 para incluir el día de cierre
            duracion = (int) ChronoUnit.DAYS.between(FechaIncio, FechaFin) + 1;
        }

        return duracion;


    }

    // Funcion auxiliar para mapear de la capa de entidad a la capa de dto
    private FestivalDTO mapearADTO(Festival festival) {
        // Calculamos la duración total en días (incluyendo el día de inicio y de fin)
        int duracion=CalcularDuracion(festival.getFechaInicio(),festival.getFechaFin());

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



// API - REST TRANSACCIONES
        //Funcion auxiliar para mapear de un FestivalDto a un FestivalResponseDTO
        private FestivalResponseDTO mapearRequestDTO(FestivalDTO festivalAux){
         FestivalResponseDTO festivalResponse= new FestivalResponseDTO();    
        festivalResponse.setId(festivalAux.getId());
        festivalResponse.setNombre(festivalAux.getNombre());
        festivalResponse.setTemporada(festivalAux.getTemporada());
        festivalResponse.setDuracionDias(festivalAux.getDuracionDias());
        festivalResponse.setFechaInicio(festivalAux.getFechaInicio());
        festivalResponse.setFechaFin(festivalAux.getFechaFin());
        return festivalResponse;    

        }

        @Transactional(readOnly = true)
        public FestivalResponseDTO obternerPorNombreTemporadaFecha (String nombre,String temporada,LocalDate fechaInicio){
        Festival festival = repository.findByNombreAndTemporadaAndFechaInicio(nombre, temporada,fechaInicio)
        .orElseThrow(() -> new RuntimeException("Festival no encontrado "));

        FestivalDTO festivalAux =mapearADTO(festival);
        return(mapearRequestDTO(festivalAux));

        
        }

        @Override
        public FestivalResponseDTO crearFestival(FestivalRequestDTO requestDTO){
            
            Festival festival=new Festival();
            festival.setNombre(requestDTO.getNombre());
            festival.setTemporada(requestDTO.getTemporada());
            festival.setFechaInicio(requestDTO.getFechaInicio());
            festival.setFechaFin(requestDTO.getFechaFin());
            
            Festival festivalGuardado=repository.save(festival);

            FestivalDTO festivalAux =mapearADTO(festivalGuardado);
            return(mapearRequestDTO(festivalAux));
        }

}