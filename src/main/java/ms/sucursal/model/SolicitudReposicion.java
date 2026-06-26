package ms.sucursal.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "solicitudes_reposicion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudReposicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idSucursal;   // FK lógica hacia Sucursal
    private String estado;     // "Pendiente", "Aprobada", "Rechazada", "Enviada", "Recibida"

    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;

    // ---------- FUTURA CONEXIÓN CON PROVEEDOR E INVENTARIO ----------
    // Será utilizada cuando se implementen los módulos de Proveedores y Envíos.
}
