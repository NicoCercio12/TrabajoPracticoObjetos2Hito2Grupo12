package com.grupo12.epicentro_gourmet.dtos;
import java.time.LocalDate;


public class FestivalRequestDTO {

    private String nombre;
    private String temporada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

   

    public FestivalRequestDTO() {
        
    }

    public String getNombre() {
        return nombre;
    }
    public String getTemporada() {
        return temporada;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

     public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

  
    
   
    
}
