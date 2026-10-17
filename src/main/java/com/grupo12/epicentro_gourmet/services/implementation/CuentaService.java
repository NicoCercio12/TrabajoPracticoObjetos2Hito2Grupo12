package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.User;
import com.grupo12.epicentro_gourmet.entities.UserRole;
import com.grupo12.epicentro_gourmet.helpers.Roles;
import com.grupo12.epicentro_gourmet.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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
    public User crear(String email, String password){
        User user = new User();
        user.setUsername(email.trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setUserRoles(new HashSet<>());
        user.getUserRoles().add(new UserRole(user, Roles.PENDIENTE));
        return userRepository.save(user);
    }
}
