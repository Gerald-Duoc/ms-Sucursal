package ms.sucursal.model;

import lombok.Data;
import java.util.Date;

@Data
public class SolicitudReposicionDTO {
    private Long id;
    private Long idSucursal;
    private String estado;
    private Date fechaSolicitud;
}