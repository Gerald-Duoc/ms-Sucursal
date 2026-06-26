package ms.sucursal.repository;

import ms.sucursal.model.SolicitudReposicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudReposicionRepository extends JpaRepository<SolicitudReposicion, Long> {
    List<SolicitudReposicion> findByIdSucursal(Long idSucursal);
}
