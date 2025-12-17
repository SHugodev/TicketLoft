package com.ticketloft.config;

import com.ticketloft.model.Categoria;
import com.ticketloft.model.Usuario;
import com.ticketloft.repository.CategoriaRepository;
import com.ticketloft.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear categorías si no existen
        if (categoriaRepository.count() == 0) {
            categoriaRepository.save(new Categoria("Música", "Conciertos y festivales", "🎵"));
            categoriaRepository.save(new Categoria("Deportes", "Eventos deportivos", "⚽"));
            categoriaRepository.save(new Categoria("Teatro", "Obras y espectáculos", "🎭"));
            categoriaRepository.save(new Categoria("Conferencias", "Charlas profesionales", "🎤"));
            categoriaRepository.save(new Categoria("Tecnología", "Eventos tech", "💻"));
            
            System.out.println("✅ Categorías inicializadas");
        }

        // Crear usuario admin si no existe
        if (!usuarioRepository.existsByEmail("admin@ticketloft.com")) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@ticketloft.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(Usuario.Rol.ADMIN);
            admin.setActivo(true);
            
            usuarioRepository.save(admin);
            
            System.out.println("✅ Usuario admin creado:");
            System.out.println("   Email: admin@ticketloft.com");
            System.out.println("   Contraseña: admin123");
        }

        // Crear usuario de prueba si no existe
        if (!usuarioRepository.existsByEmail("user@ticketloft.com")) {
            Usuario user = new Usuario();
            user.setNombre("Usuario Demo");
            user.setEmail("user@ticketloft.com");
            user.setContrasena(passwordEncoder.encode("user123"));
            user.setRol(Usuario.Rol.USUARIO);
            user.setActivo(true);
            
            usuarioRepository.save(user);
            
            System.out.println("✅ Usuario demo creado:");
            System.out.println("   Email: user@ticketloft.com");
            System.out.println("   Contraseña: user123");
        }
    }
}