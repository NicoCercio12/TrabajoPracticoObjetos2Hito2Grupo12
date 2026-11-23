package com.grupo12.epicentro_gourmet.dtos;
import java.util.List;

import com.grupo12.epicentro_gourmet.entities.Plato;
import com.grupo12.epicentro_gourmet.entities.Empleado;
public class ModificarPlatoDeUnidadResponseDTO {
    
   
    private Long id;

   
    private String codigoUnico;

    
    private String nombreComercial;

    
    private double superficie;

    
    private String tipoUnidad;

    private Plato plato;
   
    // ==========================================
    // ATRIBUTOS ESPECÍFICOS DE FOOD TRUCK
    // ==========================================
    private String patente;
    private Boolean requiereElectricidad;

    // ==========================================
    // ATRIBUTOS ESPECÍFICOS DE PUESTO DESARMABLE
    // ==========================================
    private Integer cantCarpas;
    private Integer tiempoMontajeMinutos;

    // ==========================================
    // CONSTRUCTOR VACIO
    // ==========================================
    public ModificarPlatoDeUnidadResponseDTO() {
    }
    
    // ==========================================
    //GETTERS Y SETTERS
    // ==========================================
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCodigoUnico() {
        return codigoUnico;
    }
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    public String getNombreComercial() {
        return nombreComercial;
    }
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }
    public double getSuperficie() {
        return superficie;
    }
    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }
    public String getTipoUnidad() {
        return tipoUnidad;
    }
    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
   
    public String getPatente() {
        return patente;
    }
    public void setPatente(String patente) {
        this.patente = patente;
    }
    public Boolean getRequiereElectricidad() {
        return requiereElectricidad;
    }
    public void setRequiereElectricidad(Boolean requiereElectricidad) {
        this.requiereElectricidad = requiereElectricidad;
    }
    public Integer getCantCarpas() {
        return cantCarpas;
    }
    public void setCantCarpas(Integer cantCarpas) {
        this.cantCarpas = cantCarpas;
    }
    public Integer getTiempoMontajeMinutos() {
        return tiempoMontajeMinutos;
    }
    public void setTiempoMontajeMinutos(Integer tiempoMontajeMinutos) {
        this.tiempoMontajeMinutos = tiempoMontajeMinutos;
    }
    public Plato getPlato() {
        return plato;
    }
    public void setPlato(Plato plato) {
        this.plato = plato;
    }



    
}
