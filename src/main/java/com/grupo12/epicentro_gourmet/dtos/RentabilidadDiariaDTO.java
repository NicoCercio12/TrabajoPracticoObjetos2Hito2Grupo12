
package com.grupo12.epicentro_gourmet.dtos;

import java.time.LocalDate;

public class RentabilidadDiariaDTO {

    private LocalDate fecha;
    private double recaudacion;
    private double costoPlatos;
    private double sueldos;
    private double canon;
    private double rentabilidadNeta;
    private double rentabilidadAcumulada;

    public RentabilidadDiariaDTO(
            LocalDate fecha,
            Double recaudacion,
            Double costoPlatos) {
        this.fecha = fecha;
        this.recaudacion = recaudacion != null ? recaudacion : 0.0;
        this.costoPlatos = costoPlatos != null ? costoPlatos : 0.0;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getRecaudacion() {
        return recaudacion;
    }

    public double getCostoPlatos() {
        return costoPlatos;
    }

    public double getSueldos() {
        return sueldos;
    }

    public double getCanon() {
        return canon;
    }

    public double getRentabilidadNeta() {
        return rentabilidadNeta;
    }

    public double getRentabilidadAcumulada() {
        return rentabilidadAcumulada;
    }

    public void setSueldos(double sueldos) {
        this.sueldos = sueldos;
    }

    public void setCanon(double canon) {
        this.canon = canon;
    }

    public void setRentabilidadNeta(double rentabilidadNeta) {
        this.rentabilidadNeta = rentabilidadNeta;
    }

    public void setRentabilidadAcumulada(double rentabilidadAcumulada) {
        this.rentabilidadAcumulada = rentabilidadAcumulada;
    }
}
