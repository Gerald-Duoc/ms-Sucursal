package ms.sucursal.model;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class TransferenciaDTO {
    private Long id;
    private Long idSucursalOrigen;
    private Long idSucursalDestino;
    private String estado;
    private Date fechaSolicitud;
    private Long idEmpleadoRecepcion;
    private List<ItemTransferencia> items;
}