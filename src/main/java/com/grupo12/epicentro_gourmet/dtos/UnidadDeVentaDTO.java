package com.grupo12.epicentro_gourmet.dtos;

// Lombok para ahorrar código repetitivo
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnidadDeVentaDTO {

    // Identificador único de base de datos
    private Long id;

    // Código alfanumérico único de 10 caracteres validado
    private String codigoUnico;

    // Nombre de fantasía o comercial del puesto
    private String nombreComercial;

    // Superficie ocupada en metros cuadrados
    private double superficie;

    // Etiqueta para distinguir el tipo en la vista ("Food Truck" o "Puesto Desarmable")
    private String tipoUnidad;

    // Nombre y apellido o email del responsable asignado
    private String responsable;

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
}