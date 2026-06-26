package ms.sucursal.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idAdminGeneral;   // FK lógica hacia Registro Usuarios
    private Long idGerenteSede;    // FK lógica hacia Registro Usuarios
    private String nombre;
    private String direccion;

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    private String telefono;
    private String email;
    private Boolean estado;

    // ---------- FUTURA CONEXIÓN CON REGISTRO USUARIOS (US-SUC-04) ----------
    // Al asignar un gerente (idGerenteSede), se validará en Registro Usuarios
    // que el usuario exista y tenga el rol "Gerente de sede".
}