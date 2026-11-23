package com.grupo12.epicentro_gourmet.dtos;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.grupo12.epicentro_gourmet.enums.TipoUnidad;


@JsonPropertyOrder({
    "id",
    "codigoUnico",
    "nombreComercial",
    "tipoUnidad",
    "superficie",
    "lstEmpleados"
})
public class ConsultarAntiguedadEmpleadosResponseDTO {
    
    private Long id;

   
    private String codigoUnico;

    
    private String nombreComercial;

    
    private double superficie;

    
    private TipoUnidad tipoUnidad;

    Set<EmpleadoUnidadDTO>lstEmpleados= new HashSet<>();

    public ConsultarAntiguedadEmpleadosResponseDTO() {
    }

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

   

  

    public Set<EmpleadoUnidadDTO> getLstEmpleados() {
        return lstEmpleados;
    }

    public void setLstEmpleados(Set<EmpleadoUnidadDTO> lstEmpleados) {
        this.lstEmpleados = lstEmpleados;
    }

    public TipoUnidad getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TipoUnidad tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }




}
