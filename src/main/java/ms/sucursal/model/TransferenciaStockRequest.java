package ms.sucursal.model;

import lombok.Data;

@Data
public class TransferenciaStockRequest {
    private Long idSucursalOrigen;
    private Long idSucursalDestino;
    private Long idLibro;
    private int cantidad;
}