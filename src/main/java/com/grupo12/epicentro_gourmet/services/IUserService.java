package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User insertOrUpdate(User user);
    boolean remove(Long id);

}
