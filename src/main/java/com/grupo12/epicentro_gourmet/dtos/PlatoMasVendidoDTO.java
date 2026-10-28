package com.grupo12.epicentro_gourmet.dtos;

public class PlatoMasVendidoDTO {
    private String nombrePlato;
    private Long cantidadVendida;
    private Double totalRecaudado;

    // Constructor que usa la consulta JPQL directamente
    public PlatoMasVendidoDTO(String nombrePlato, Long cantidadVendida, Double totalRecaudado) {
        this.nombrePlato = nombrePlato;
        this.cantidadVendida = cantidadVendida;
        this.totalRecaudado = totalRecaudado;
    }

    public String getNombrePlato() { return nombrePlato; }
    public void setNombrePlato(String nombrePlato) { this.nombrePlato = nombrePlato; }

    public Long getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(Long cantidadVendida) { this.cantidadVendida = cantidadVendida; }

    public Double getTotalRecaudado() { return totalRecaudado; }
    public void setTotalRecaudado(Double totalRecaudado) { this.totalRecaudado = totalRecaudado; }
}