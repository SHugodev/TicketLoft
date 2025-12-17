package com.ticketloft.service;

import com.ticketloft.model.Usuario;
import com.ticketloft.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtener usuario actual autenticado
     */
    public Optional<Usuario> obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        String email = authentication.getName();
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtener usuario por ID
     */
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Obtener usuario por email
     */
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtener todos los usuarios (admin)
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtener usuarios por rol
     */
    public List<Usuario> obtenerUsuariosPorRol(Usuario.Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Contar usuarios totales
     */
    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    /**
     * Contar usuarios activos
     */
    public long contarUsuariosActivos() {
        return usuarioRepository.findByActivo(true).size();
    }

    /**
     * Activar/Desactivar usuario (admin)
     */
    public void toggleActivoUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);
    }

    /**
     * Cambiar rol de usuario (admin)
     */
    public void cambiarRolUsuario(Long id, Usuario.Rol nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
    }
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}