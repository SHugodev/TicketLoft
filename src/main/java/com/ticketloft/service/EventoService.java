package com.ticketloft.service;

import com.ticketloft.model.*;
import com.ticketloft.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TipoEntradaRepository tipoEntradaRepository;

    /**
     * Obtener todos los eventos aprobados y futuros
     */
    public List<Evento> obtenerEventosPublicos() {
        return eventoRepository.findEventosAprobadosYFuturos(LocalDateTime.now());
    }

    /**
     * Obtener todos los eventos (para admin)
     */
    public List<Evento> obtenerTodosLosEventos() {
        return eventoRepository.findAll();
    }

    /**
     * Obtener eventos por categoría
     */
    public List<Evento> obtenerEventosPorCategoria(Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return eventoRepository.findByCategoria(categoria);
    }

    /**
     * Obtener eventos por ciudad
     */
    public List<Evento> obtenerEventosPorCiudad(String ciudad) {
        return eventoRepository.findByCiudad(ciudad);
    }

    /**
     * Obtener evento por ID
     */
    public Optional<Evento> obtenerEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    /**
     * Obtener eventos creados por un usuario
     */
    public List<Evento> obtenerEventosPorUsuario(Usuario usuario) {
        return eventoRepository.findByCreadoPor(usuario);
    }

    /**
     * Crear nuevo evento
     */
    @Transactional
    public Evento crearEvento(Evento evento, Usuario creador) {
        evento.setCreadoPor(creador);
        evento.setFechaCreacion(LocalDateTime.now());
        evento.setActivo(true);
        evento.setEstadoAprobacion(Evento.EstadoAprobacion.PENDIENTE);
        return eventoRepository.save(evento);
    }

    /**
     * Actualizar evento
     */
    @Transactional
    public Evento actualizarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    /**
     * Eliminar evento (soft delete)
     */
    @Transactional
    public void eliminarEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        evento.setActivo(false);
        eventoRepository.save(evento);
    }

    /**
     * Eliminar evento definitivamente (hard delete) - Para admin
     */
    @Transactional
    public void eliminarEventoDefinitivamente(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Eliminar tipos de entrada asociados
        List<TipoEntrada> tiposEntrada = tipoEntradaRepository.findByEvento(evento);
        tipoEntradaRepository.deleteAll(tiposEntrada);

        // Eliminar evento (las reservas se eliminan por cascade si está configurado,
        // pero por seguridad si falla, el delete del evento fallaría)
        eventoRepository.delete(evento);
    }

    /**
     * Aprobar evento (solo admin)
     */
    @Transactional
    public void aprobarEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        evento.setEstadoAprobacion(Evento.EstadoAprobacion.APROBADO);
        eventoRepository.save(evento);
    }

    /**
     * Rechazar evento (solo admin)
     */
    @Transactional
    public void rechazarEvento(Long id, String motivo) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        evento.setEstadoAprobacion(Evento.EstadoAprobacion.RECHAZADO);
        evento.setMotivoRechazo(motivo);
        eventoRepository.save(evento);
    }

    /**
     * Obtener eventos pendientes de aprobación
     */
    public List<Evento> obtenerEventosPendientes() {
        return eventoRepository.findByEstadoAprobacion(Evento.EstadoAprobacion.PENDIENTE);
    }

    /**
     * Obtener todas las categorías
     */
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * Obtener tipos de entrada de un evento
     */
    public List<TipoEntrada> obtenerTiposEntradaPorEvento(Evento evento) {
        return tipoEntradaRepository.findByEventoAndActivoTrue(evento);
    }

    /**
     * Obtener categoría por ID
     */
    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Contar eventos totales
     */
    public long contarEventos() {
        return eventoRepository.count();
    }

    /**
     * Contar eventos activos
     */
    public long contarEventosActivos() {
        return eventoRepository.findByActivoTrue().size();
    }
}