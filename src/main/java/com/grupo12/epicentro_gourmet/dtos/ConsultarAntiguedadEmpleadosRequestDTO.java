package com.grupo12.epicentro_gourmet.dtos;

public class ConsultarAntiguedadEmpleadosRequestDTO {
    String codigoUnico;
    int antiguedadConsultada;
    String estado;
    
    public ConsultarAntiguedadEmpleadosRequestDTO() {
    }
    public String getCodigoUnico() {
        return codigoUnico;
    }
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    public int getAntiguedadConsultada() {
        return antiguedadConsultada;
    }
    public void setAntiguedadConsultada(int antiguedadConsultada) {
        this.antiguedadConsultada = antiguedadConsultada;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
