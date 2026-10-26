package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByUsuario(User usuario);
    List<Empleado> findByActivo(boolean activo);
    boolean existsByDni(String dni);
    Optional<Empleado> findByDni(String dni);
    boolean existsByUsuario(User usuario);

    List<Empleado> findByUnidadAsignada_Responsable_Usuario_UsernameAndActivo(
            String username, boolean activo);
}
