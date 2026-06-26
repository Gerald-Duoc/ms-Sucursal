package ms.sucursal.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "transferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idSucursalOrigen;
    private Long idSucursalDestino;
    private String estado;  // "Pendiente", "Aprobada", "Rechazada"

    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;

    private Long idEmpleadoRecepcion;  // FK lógica hacia Registro Usuarios

    // ---------- FUTURA CONEXIÓN CON INVENTARIO (US-SUC-02) ----------
    // Antes de crear la transferencia, se debe verificar que la sucursal origen
    // tenga stock suficiente del producto, llamando a inventario-ms (US-INV-11).
}