package ms.sucursal.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String estado;

    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;

    private Long idEmpleadoRecepcion;

    // Relación con los ítems de la transferencia
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transferencia_id")
    private List<ItemTransferencia> items = new ArrayList<>();
}