package com.grupo12.epicentro_gourmet.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String temporada;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "festival_unidad_de_venta",
            joinColumns = @JoinColumn(name = "festival_id"),
            inverseJoinColumns = @JoinColumn(name = "unidad_de_venta_id"))
    private Set<UnidadDeVenta> lstUdv = new HashSet<>();

    @OneToMany(mappedBy = "festival", fetch = FetchType.LAZY)
    private Set<Pedido> lstPedidos = new HashSet<>();

    // composición: los costos viven y mueren con el festival
    @OneToMany(mappedBy = "festival", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Costo> lstCostos = new HashSet<>();

    public Festival(String nombre, String temporada, LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre = nombre;
        this.temporada = temporada;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}