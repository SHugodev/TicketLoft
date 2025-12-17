package com.ticketloft.repository;

import com.ticketloft.model.Incidencia;
import com.ticketloft.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
    List<Incidencia> findByUsuario(Usuario usuario);

    List<Incidencia> findByEstado(Incidencia.EstadoIncidencia estado);
}
