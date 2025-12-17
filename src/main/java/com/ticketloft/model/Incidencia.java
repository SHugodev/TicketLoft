package com.ticketloft.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String asunto;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Puede ser null si permitimos anónimos, pero por ahora requeriremos login

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIncidencia estado = EstadoIncidencia.PENDIENTE;

    public enum EstadoIncidencia {
        PENDIENTE, RESUELTA, EN_PROCESO
    }

    public Incidencia() {
        this.fecha = LocalDateTime.now();
    }

    public Incidencia(String asunto, String descripcion, Usuario usuario) {
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoIncidencia.PENDIENTE;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidencia estado) {
        this.estado = estado;
    }
}
