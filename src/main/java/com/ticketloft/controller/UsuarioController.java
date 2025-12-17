package com.ticketloft.controller;

import com.ticketloft.model.Usuario;
import com.ticketloft.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Ver perfil del usuario actual
     */
    @GetMapping
    public String verPerfil(Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);
        
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    /**
     * Actualizar datos del perfil
     */
    @PostMapping("/actualizar")
    public String actualizarPerfil(
            @RequestParam String nombre,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {
        
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);
        
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        try {
            // Verificar si el email ya está en uso por otro usuario
            if (!usuario.getEmail().equals(email)) {
                if (usuarioService.existeEmail(email)) {
                    redirectAttributes.addFlashAttribute("error", "El email ya está en uso");
                    return "redirect:/perfil";
                }
            }

            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuarioService.guardarUsuario(usuario);

            redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar perfil: " + e.getMessage());
        }

        return "redirect:/perfil";
    }

    /**
     * Cambiar contraseña
     */
    @PostMapping("/cambiar-contrasena")
    public String cambiarContrasena(
            @RequestParam String contrasenaActual,
            @RequestParam String contrasenaNueva,
            @RequestParam String contrasenaConfirmar,
            RedirectAttributes redirectAttributes) {
        
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);
        
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        try {
            // Verificar contraseña actual
            if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena())) {
                redirectAttributes.addFlashAttribute("errorPassword", "La contraseña actual es incorrecta");
                return "redirect:/perfil";
            }

            // Verificar que las contraseñas coinciden
            if (!contrasenaNueva.equals(contrasenaConfirmar)) {
                redirectAttributes.addFlashAttribute("errorPassword", "Las contraseñas no coinciden");
                return "redirect:/perfil";
            }

            // Verificar longitud mínima
            if (contrasenaNueva.length() < 6) {
                redirectAttributes.addFlashAttribute("errorPassword", "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/perfil";
            }

            // Actualizar contraseña
            usuario.setContrasena(passwordEncoder.encode(contrasenaNueva));
            usuarioService.guardarUsuario(usuario);

            redirectAttributes.addFlashAttribute("successPassword", "Contraseña actualizada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorPassword", "Error al cambiar contraseña");
        }

        return "redirect:/perfil";
    }

    /**
     * Eliminar cuenta
     */
    @PostMapping("/eliminar-cuenta")
    public String eliminarCuenta(
            @RequestParam String contrasenaConfirmacion,
            RedirectAttributes redirectAttributes) {
        
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);
        
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        try {
            // Verificar contraseña
            if (!passwordEncoder.matches(contrasenaConfirmacion, usuario.getContrasena())) {
                redirectAttributes.addFlashAttribute("error", "Contraseña incorrecta");
                return "redirect:/perfil";
            }

            // Desactivar cuenta (no eliminar físicamente)
            usuario.setActivo(false);
            usuarioService.guardarUsuario(usuario);

            redirectAttributes.addFlashAttribute("success", "Cuenta eliminada correctamente");
            return "redirect:/auth/logout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar cuenta");
            return "redirect:/perfil";
        }
    }
}