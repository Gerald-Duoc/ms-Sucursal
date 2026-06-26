package ms.sucursal.model;

import java.util.Date;

import lombok.Data;

@Data
public class SucursalDTO {
    private Long id;
    private Long idAdminGeneral;
    private Long idGerenteSede;
    private String nombre;
    private String direccion;
    private Date fechaInicio;
    private String telefono;
    private String email;
    private Boolean estado;
}