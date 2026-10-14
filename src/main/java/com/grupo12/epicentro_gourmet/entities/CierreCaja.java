package com.grupo12.epicentro_gourmet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class CierreCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate fecha;
    private double totalRecaudado;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cajero cajero;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
