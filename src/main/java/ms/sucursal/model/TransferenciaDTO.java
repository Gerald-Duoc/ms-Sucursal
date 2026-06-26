package ms.sucursal.model;

import lombok.Data;
import java.util.Date;

@Data
public class TransferenciaDTO {
    private Long id;
    private Long idSucursalOrigen;
    private Long idSucursalDestino;
    private String estado;
    private Date fechaSolicitud;
    private Long idEmpleadoRecepcion;
}