package com.grupo12.epicentro_gourmet.dtos;

public class ReporteVentaDTO {
    private Long idFestival;
    private String nombreFestival;
    private double recaudacionTotal;

    public ReporteVentaDTO() {}

    public ReporteVentaDTO(Long idFestival, String nombreFestival, double recaudacionTotal) {
        this.idFestival = idFestival;
        this.nombreFestival = nombreFestival;
        this.recaudacionTotal = recaudacionTotal;
    }

    public Long getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(Long idFestival) {
        this.idFestival = idFestival;
    }

    public String getNombreFestival() {
        return nombreFestival;
    }

    public void setNombreFestival(String nombreFestival) {
        this.nombreFestival = nombreFestival;
    }

    public double getRecaudacionTotal() {
        return recaudacionTotal;
    }

    public void setRecaudacionTotal(double recaudacionTotal) {
        this.recaudacionTotal = recaudacionTotal;
    }
}