package ms.sucursal.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_transferencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTransferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idLibro;       // FK lógica hacia Libro en inventario-ms
    private int cantidad;
}