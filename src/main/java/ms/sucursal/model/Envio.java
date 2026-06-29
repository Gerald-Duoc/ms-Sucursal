package ms.sucursal.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idTransferencia;
    private String direccionOrigen;
    private String direccionDestino;

    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;
}