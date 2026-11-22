package com.grupo12.epicentro_gourmet.dtos;

public class ModificarPlatoDeUnidadRequestDTO {
    private String codigoUnico;
    private String nombrePlato;
    private double precioPlato;
    
  
    public ModificarPlatoDeUnidadRequestDTO() {
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

      public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(double precioPlato) {
        this.precioPlato = precioPlato;
    }

    
    
}
