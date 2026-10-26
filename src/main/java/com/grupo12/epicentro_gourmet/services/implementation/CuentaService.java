package com.grupo12.epicentro_gourmet.services.implementation;

import java.util.HashSet;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo12.epicentro_gourmet.entities.User;
import com.grupo12.epicentro_gourmet.entities.UserRole;
import com.grupo12.epicentro_gourmet.helpers.Roles;
import com.grupo12.epicentro_gourmet.repositories.IUserRepository;

@Service
public class CuentaService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CuentaService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existe(String email) {
        return userRepository.existsByUsername(email.trim().toLowerCase());
    }

    @Transactional
    public User crear(String email, String password) {
        User user = new User();
        user.setUsername(email.trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setUserRoles(new HashSet<>());
        user.getUserRoles().add(new UserRole(user, Roles.PENDIENTE));
        return userRepository.save(user);
    }

    @Transactional
    public User crearCuentaEmpleado(String email, String password) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "El email del empleado es obligatorio");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria");
        }

        String emailNormalizado = email.trim().toLowerCase(
                java.util.Locale.ROOT);

        if (emailNormalizado.length() > 45) {
            throw new IllegalArgumentException(
                    "El email no puede superar los 45 caracteres");
        }

        if (userRepository.existsByUsername(emailNormalizado)) {
            throw new IllegalArgumentException(
                    "Ya existe una cuenta con ese email");
        }

        User usuario = new User();
        usuario.setUsername(emailNormalizado);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEnabled(true);
        usuario.setUserRoles(new HashSet<>());

        UserRole rol = new UserRole(usuario, Roles.EMPLEADO);
        usuario.getUserRoles().add(rol);

        return userRepository.save(usuario);
    }

}