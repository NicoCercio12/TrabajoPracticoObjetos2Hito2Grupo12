package com.grupo12.epicentro_gourmet.entities;

package com.grupo12.epicentro_gourmet.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UnidadDeVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreComercial;

    @Column(unique = true, nullable = false)
    private String codigoUnico;

    private Double superficie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado responsable;

    @ManyToMany(mappedBy = "lstUdv", fetch = FetchType.LAZY)
    private Set<Festival> lstFestivales = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "unidad_de_venta_plato",
            joinColumns = @JoinColumn(name = "unidad_de_venta_id"),
            inverseJoinColumns = @JoinColumn(name = "plato_id"))
    private Set<Plato> lstPlatos = new HashSet<>();

    @OneToMany(mappedBy = "unidadAsignada", fetch = FetchType.LAZY)
    private Set<Empleado> lstStaff = new HashSet<>();

    @OneToMany(mappedBy = "unidadDeVenta", fetch = FetchType.LAZY)
    private Set<Pedido> lstPedidos = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected UnidadDeVenta(String nombreComercial, String codigoUnico, Double superficie) {
        this.nombreComercial = nombreComercial;
        this.codigoUnico = codigoUnico;
        this.superficie = superficie;
    }

    public void setCodigoUnico(String codigoUnico) {
        if (codigoUnico == null || codigoUnico.isBlank())
            throw new IllegalArgumentException("El código único no puede estar vacío");
        this.codigoUnico = codigoUnico.trim().toUpperCase();
    }

}