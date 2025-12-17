
package com.ticketloft.service;

import com.ticketloft.model.Usuario;
import com.ticketloft.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registrar un nuevo usuario
     */
    @Transactional
    public Usuario registrarUsuario(String nombre, String email, String contrasena) {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(contrasena)); // Hashear contraseña
        usuario.setRol(Usuario.Rol.USUARIO); // Por defecto USUARIO
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    /**
     * Obtener usuario por email
     */
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Verificar si un email ya está registrado
     */
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Cambiar contraseña de un usuario
     */
    @Transactional
    public void cambiarContrasena(Usuario usuario, String nuevaContrasena) {
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }
}