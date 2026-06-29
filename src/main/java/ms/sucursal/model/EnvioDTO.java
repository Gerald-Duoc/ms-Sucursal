package ms.sucursal.model;

import lombok.Data;
import java.util.Date;

@Data
public class EnvioDTO {
    private Long id;
    private Long idTransferencia;
    private String direccionOrigen;
    private String direccionDestino;
    private Date fechaSolicitud;
}