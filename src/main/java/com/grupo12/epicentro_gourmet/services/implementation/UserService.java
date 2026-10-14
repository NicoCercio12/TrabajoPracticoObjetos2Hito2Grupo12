package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.IUserRepository;
import com.grupo12.epicentro_gourmet.services.IUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserService implements IUserService, UserDetailsService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}