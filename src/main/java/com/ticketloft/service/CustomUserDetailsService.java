package com.ticketloft.service;

import com.ticketloft.model.Usuario;
import com.ticketloft.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Verificar si el usuario está activo
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario desactivado");
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasena())
                .authorities(getAuthorities(usuario))
                .accountExpired(false)
                .accountLocked(!usuario.getActivo())
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        // Spring Security requiere el prefijo "ROLE_"
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
        );
    }
}