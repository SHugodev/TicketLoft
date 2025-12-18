package com.ticketloft.service;

import com.ticketloft.model.*;
import com.ticketloft.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private TipoEntradaRepository tipoEntradaRepository;

    /**
     * Crear nueva reserva
     */
    @Transactional
    public Reserva crearReserva(Usuario usuario, Evento evento, TipoEntrada tipoEntrada, Integer cantidad) {
        // Validar que hay plazas disponibles
        if (!evento.hayPlazasDisponibles()) {
            throw new RuntimeException("No hay plazas disponibles");
        }

        // Validar que hay entradas del tipo solicitado
        if (tipoEntrada.getCantidadDisponible() < cantidad) {
            throw new RuntimeException("No hay suficientes entradas de este tipo");
        }

        // Calcular precio total con gastos de gestión (3%)
        BigDecimal precioBase = tipoEntrada.getPrecio().multiply(new BigDecimal(cantidad));
        BigDecimal gastosGestion = precioBase.multiply(new BigDecimal("0.03"));
        BigDecimal precioTotal = precioBase.add(gastosGestion);

        // Crear reserva
        Reserva reserva = new Reserva(usuario, evento, tipoEntrada, cantidad, precioTotal);
        reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
        reserva.setFechaReserva(LocalDateTime.now());

        // Actualizar cantidad disponible del tipo de entrada
        tipoEntrada.setCantidadDisponible(tipoEntrada.getCantidadDisponible() - cantidad);
        tipoEntradaRepository.save(tipoEntrada);

        return reservaRepository.save(reserva);
    }

    /**
     * Obtener reservas de un usuario
     */
    public List<Reserva> obtenerReservasPorUsuario(Usuario usuario) {
        return reservaRepository.findByUsuarioId(usuario.getId());
    }

    /**
     * Obtener reservas de un evento
     */
    public List<Reserva> obtenerReservasPorEvento(Evento evento) {
        return reservaRepository.findByEvento(evento);
    }

    /**
     * Verificar si un usuario tiene reserva en un evento
     */
    public boolean usuarioTieneReserva(Usuario usuario, Evento evento) {
        List<Reserva> reservas = reservaRepository.findByUsuarioAndEvento(usuario, evento);
        return reservas.stream()
                .anyMatch(r -> r.getEstado() == Reserva.EstadoReserva.CONFIRMADA);
    }

    /**
     * Cancelar reserva
     */
    @Transactional
    public void cancelarReserva(Long reservaId, Usuario usuario) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Verificar que la reserva pertenece al usuario
        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para cancelar esta reserva");
        }

        // Verificar que la reserva está confirmada
        if (reserva.getEstado() != Reserva.EstadoReserva.CONFIRMADA) {
            throw new RuntimeException("Esta reserva ya está cancelada");
        }

        // Devolver las entradas al stock
        TipoEntrada tipoEntrada = reserva.getTipoEntrada();
        tipoEntrada.setCantidadDisponible(tipoEntrada.getCantidadDisponible() + reserva.getCantidad());
        tipoEntradaRepository.save(tipoEntrada);

        // Cancelar reserva
        reserva.setEstado(Reserva.EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    /**
     * Obtener reserva por ID
     */
    public Optional<Reserva> obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id);
    }

    /**
     * Obtener todas las reservas (admin)
     */
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    /**
     * Contar reservas totales
     */
    public long contarReservas() {
        return reservaRepository.count();
    }

    /**
     * Contar reservas confirmadas
     */
    public long contarReservasConfirmadas() {
        return reservaRepository.findByEstado(Reserva.EstadoReserva.CONFIRMADA).size();
    }
}