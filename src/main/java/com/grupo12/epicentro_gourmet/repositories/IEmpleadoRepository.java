
package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Empleado> findByUnidadAsignada_Id(Long idUnidad);

    @Query("""
        SELECT e
        FROM Empleado e
        JOIN FETCH e.unidadAsignada u
        WHERE u.responsable.usuario.username = :username
          AND e.activo = true
          AND (:idUnidad IS NULL OR u.id = :idUnidad)
          AND (
              :texto = ''
              OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
              OR LOWER(e.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))
              OR e.dni LIKE CONCAT('%', :texto, '%')
          )
        ORDER BY e.apellido, e.nombre
        """)
    List<Empleado> filtrarActivosPorResponsable(
            @Param("username") String username,
            @Param("texto") String texto,
            @Param("idUnidad") Long idUnidad);
}
