package ms.sucursal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ms.sucursal.model.Transferencia;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByIdSucursalOrigenOrIdSucursalDestino(Long idOrigen, Long idDestino);
}