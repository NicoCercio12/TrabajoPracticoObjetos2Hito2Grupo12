package com.grupo12.epicentro_gourmet.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.grupo12.epicentro_gourmet.enums.TipoCosto;
import com.grupo12.epicentro_gourmet.enums.TipoUnidad;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Costo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoCosto tipo;

    private double monto;

    @Enumerated(EnumType.STRING)
    private TipoUnidad tipoUnidadAplicable;   // null = aplica a todas

    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Costo(TipoCosto tipo, double monto, TipoUnidad tipoUnidadAplicable, Festival festival) {
        this.tipo = tipo;
        this.monto = monto;
        this.tipoUnidadAplicable = tipoUnidadAplicable;
        this.festival = festival;
    }

}