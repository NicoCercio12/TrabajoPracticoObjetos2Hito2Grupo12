package com.grupo12.epicentro_gourmet.dtos;
public class RankingUnidadDTO {

    private Long idUnidad;
    private String nombreComercial;
    private String codigoUnico;
    private double recaudacion;

    public RankingUnidadDTO(
            Long idUnidad,
            String nombreComercial,
            String codigoUnico,
            double recaudacion) {
        this.idUnidad = idUnidad;
        this.nombreComercial = nombreComercial;
        this.codigoUnico = codigoUnico;
        this.recaudacion = recaudacion;
    }

    public Long getIdUnidad() {
        return idUnidad;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public double getRecaudacion() {
        return recaudacion;
    }
}
