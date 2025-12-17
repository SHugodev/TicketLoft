package com.ticketloft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Recursos públicos
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/fonts/**", "/favicon.ico")
                        .permitAll()

                        // Páginas públicas
                        .requestMatchers("/", "/index", "/login", "/register").permitAll()

                        // API de autenticación pública
                        .requestMatchers("/api/auth/register", "/api/auth/check-email").permitAll()

                        // Eventos públicos (solo lectura)
                        .requestMatchers("/eventos", "/eventos/{id:[0-9]+}").permitAll()

                        // Panel de administración solo para ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Crear eventos - accesible para usuarios autenticados Y admin
                        .requestMatchers("/eventos/crear", "/eventos/guardar", "/eventos/editar/**").authenticated()

                        // Gestión de eventos propios (autenticado)
                        .requestMatchers("/mis-eventos", "/eventos/mis-eventos").authenticated()
                        .requestMatchers("/eventos/eliminar/**").authenticated()

                        // Reservas solo para usuarios autenticados
                        .requestMatchers("/reservas/**").authenticated()

                        // Perfil de usuario
                        .requestMatchers("/perfil/**").authenticated()

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/api/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("contrasena")
                        .successHandler((request, response, authentication) -> {
                            // Redirigir según el rol
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())

                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false));

        return http.build();
    }
}