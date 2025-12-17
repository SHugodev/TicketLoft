package com.ticketloft.controller;

import com.ticketloft.model.Usuario;
import com.ticketloft.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        try {
            String nombre = userData.get("nombre");
            String email = userData.get("email");
            String contrasena = userData.get("contrasena");

            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre es obligatorio"));
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El email es obligatorio"));
            }
            if (contrasena == null || contrasena.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "La contraseña debe tener al menos 6 caracteres"));
            }

            // Verificar si el email ya existe
            if (authService.emailExiste(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "El email ya está registrado"));
            }

            // Registrar usuario
            Usuario usuario = authService.registrarUsuario(nombre, email, contrasena);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario registrado correctamente");
            response.put("usuario", Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail(),
                "rol", usuario.getRol().name()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar usuario: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener el usuario actual autenticado
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No hay usuario autenticado"));
        }

        String email = authentication.getName();
        Usuario usuario = authService.obtenerUsuarioPorEmail(email)
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("nombre", usuario.getNombre());
        response.put("email", usuario.getEmail());
        response.put("rol", usuario.getRol().name());
        response.put("activo", usuario.getActivo());

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para verificar si el email ya existe
     */
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean existe = authService.emailExiste(email);
        return ResponseEntity.ok(Map.of("existe", existe));
    }
}
