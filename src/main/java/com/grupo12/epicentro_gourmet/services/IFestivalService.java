package com.grupo12.epicentro_gourmet.services;
import com.grupo12.epicentro_gourmet.dtos.FestivalDTO;
import com.grupo12.epicentro_gourmet.dtos.FestivalRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.FestivalResponseDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import org.jspecify.annotations.Nullable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface IFestivalService {
    
    

    //Crear Festival desde la Api
    FestivalResponseDTO crearFestival(FestivalRequestDTO requestDTO);

    List<Festival> getAll();
    Optional<Festival> findById(Long id);
    Festival insertOrUpdate(Festival festival);
    boolean remove(Long id);
    List<Festival> findActivos();

    // Devuelve todos los festivales convertidos a FestivalDTO ordenados por fecha
    List<FestivalDTO> obtenerTodosDTO();

    // Devuelve un festival puntual convertido a FestivalDTO por su ID
    FestivalDTO obtenerPorIdDTO(Long id);


    //Devuelve un de festival en relacion a la request del cliente  
    FestivalResponseDTO obternerPorNombreTemporadaFecha(String nombre,String temporada,LocalDate fechaInicio);

}
