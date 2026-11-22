package com.grupo12.epicentro_gourmet.dtos;

import java.time.LocalDate;

public class FestivalResponseDTO {

    private Long id;
    private String nombre;
    private String temporada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int duracionDias;
    private boolean activo;


    

    public FestivalResponseDTO() {
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getTemporada() {
        return temporada;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public int getDuracionDias() {
        return duracionDias;
    }
    public boolean isActivo() {
        return activo;
    }
    


    
}
