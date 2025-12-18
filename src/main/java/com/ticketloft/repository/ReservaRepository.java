package com.ticketloft.repository;

import com.ticketloft.model.Reserva;
import com.ticketloft.model.Usuario;
import com.ticketloft.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Buscar todas las reservas de un usuario
    List<Reserva> findByUsuario(Usuario usuario);

    // Buscar todas las reservas de un usuario por ID
    List<Reserva> findByUsuarioId(Long usuarioId);

    // Buscar todas las reservas de un evento
    List<Reserva> findByEvento(Evento evento);

    // Buscar reservas de usuario en evento (puede tener varias con diferentes
    // tipos)
    List<Reserva> findByUsuarioAndEvento(Usuario usuario, Evento evento);

    // Contar reservas confirmadas de un evento
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.evento = :evento AND r.estado = 'CONFIRMADA'")
    long countReservasConfirmadasByEvento(Evento evento);

    // Buscar reservas por estado
    List<Reserva> findByEstado(Reserva.EstadoReserva estado);

    // Buscar reservas de un usuario por estado
    List<Reserva> findByUsuarioAndEstado(Usuario usuario, Reserva.EstadoReserva estado);
}