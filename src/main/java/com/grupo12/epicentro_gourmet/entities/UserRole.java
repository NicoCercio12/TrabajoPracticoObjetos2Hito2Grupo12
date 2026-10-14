package com.grupo12.epicentro_gourmet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="user_role", uniqueConstraints=@UniqueConstraint(columnNames= {"role", "user_id"}))
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="role", nullable=false, length=100)
    private String role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UserRole(int id, User user, String role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

}
