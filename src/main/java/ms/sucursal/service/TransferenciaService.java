package ms.sucursal.service;

import lombok.RequiredArgsConstructor;
import ms.sucursal.model.Transferencia;
import ms.sucursal.model.TransferenciaDTO;
import ms.sucursal.repository.TransferenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository repository;

    private TransferenciaDTO toDTO(Transferencia t) {
        TransferenciaDTO dto = new TransferenciaDTO();
        dto.setId(t.getId());
        dto.setIdSucursalOrigen(t.getIdSucursalOrigen());
        dto.setIdSucursalDestino(t.getIdSucursalDestino());
        dto.setEstado(t.getEstado());
        dto.setFechaSolicitud(t.getFechaSolicitud());
        dto.setIdEmpleadoRecepcion(t.getIdEmpleadoRecepcion());
        return dto;
    }

    private Transferencia toEntity(TransferenciaDTO dto) {
        Transferencia t = new Transferencia();
        t.setId(dto.getId());
        t.setIdSucursalOrigen(dto.getIdSucursalOrigen());
        t.setIdSucursalDestino(dto.getIdSucursalDestino());
        t.setEstado(dto.getEstado());
        t.setFechaSolicitud(dto.getFechaSolicitud());
        t.setIdEmpleadoRecepcion(dto.getIdEmpleadoRecepcion());
        return t;
    }

    public List<TransferenciaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TransferenciaDTO> buscar(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public TransferenciaDTO guardar(TransferenciaDTO dto) {
        // ---------- FUTURA CONEXIÓN CON INVENTARIO (US-SUC-02) ----------
        // Validar stock en sucursal origen llamando a inventario-ms (US-INV-11).
        Transferencia t = toEntity(dto);
        t.setId(null);
        return toDTO(repository.save(t));
    }

    public TransferenciaDTO actualizar(Long id, TransferenciaDTO dto) {
        Transferencia existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferencia no encontrada"));
        existente.setIdSucursalOrigen(dto.getIdSucursalOrigen());
        existente.setIdSucursalDestino(dto.getIdSucursalDestino());
        existente.setEstado(dto.getEstado());
        existente.setFechaSolicitud(dto.getFechaSolicitud());
        existente.setIdEmpleadoRecepcion(dto.getIdEmpleadoRecepcion());

        // ---------- FUTURA CONEXIÓN CON INVENTARIO (US-SUC-03) ----------
        // Si el estado cambia a "Aprobada", se debe llamar a inventario-ms
        // para transferir el stock entre sucursales (US-INV-05).

        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<TransferenciaDTO> listarPorSucursal(Long idSucursal) {
        return repository.findByIdSucursalOrigenOrIdSucursalDestino(idSucursal, idSucursal)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}