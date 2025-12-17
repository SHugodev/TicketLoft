package com.ticketloft.repository;

import com.ticketloft.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por email (para login)
    Optional<Usuario> findByEmail(String email);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Buscar usuarios por rol
    List<Usuario> findByRol(Usuario.Rol rol);
    
    // Buscar usuarios activos
    List<Usuario> findByActivo(Boolean activo);
}