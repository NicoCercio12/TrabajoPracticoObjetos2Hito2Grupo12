package com.grupo12.epicentro_gourmet.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.grupo12.epicentro_gourmet.entities.User;
import com.grupo12.epicentro_gourmet.entities.UserRole;
import com.grupo12.epicentro_gourmet.repositories.IUserRepository;
import com.grupo12.epicentro_gourmet.services.IUserService;

@Service
public class UserService implements IUserService, UserDetailsService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsernameAndFetchUserRolesEagerly(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.isEnabled(),
                true, true, true, buildGrantedAuthorities(user.getUserRoles()));
    }

    private Set<GrantedAuthority> buildGrantedAuthorities(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole()))
                .collect(Collectors.toSet());
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }
    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsernameAndFetchUserRolesEagerly(username);
    }

    @Override
    public User insertOrUpdate(User user) {
        return repository.save(user);
    }

    @Override
    public boolean remove(Long id) {
        Optional<User> opt = repository.findById(id);
        if (opt.isEmpty()) return false;
        opt.get().setEnabled(false);   // baja lógica
        repository.save(opt.get());
        return true;
    }
}