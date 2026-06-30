package ms.sucursal.service;

import lombok.RequiredArgsConstructor;
import ms.sucursal.model.EnvioDTO;
import ms.sucursal.model.ItemTransferencia;
import ms.sucursal.model.Transferencia;
import ms.sucursal.model.TransferenciaDTO;
import ms.sucursal.model.TransferenciaStockRequest;
import ms.sucursal.repository.TransferenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository repository;
    private final RestTemplate restTemplate;

    private TransferenciaDTO toDTO(Transferencia t) {
        TransferenciaDTO dto = new TransferenciaDTO();
        dto.setId(t.getId());
        dto.setIdSucursalOrigen(t.getIdSucursalOrigen());
        dto.setIdSucursalDestino(t.getIdSucursalDestino());
        dto.setEstado(t.getEstado());
        dto.setFechaSolicitud(t.getFechaSolicitud());
        dto.setIdEmpleadoRecepcion(t.getIdEmpleadoRecepcion());
        dto.setItems(t.getItems()); // Asigna los ítems que vienen en la entidad
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
        Transferencia t = toEntity(dto);
        t.setId(null);
        t.setItems(dto.getItems()); // Asigna los ítems que vienen en el DTO
        return toDTO(repository.save(t));
    }

    public TransferenciaDTO actualizar(Long id, TransferenciaDTO dto) {
        Transferencia existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferencia no encontrada"));

        String estadoAnterior = existente.getEstado();

        existente.setIdSucursalOrigen(dto.getIdSucursalOrigen());
        existente.setIdSucursalDestino(dto.getIdSucursalDestino());
        existente.setEstado(dto.getEstado());
        existente.setFechaSolicitud(dto.getFechaSolicitud());
        existente.setIdEmpleadoRecepcion(dto.getIdEmpleadoRecepcion());

    // Si la transferencia pasa a "Aprobada", mover el stock de cada ítem
        if ("Aprobada".equalsIgnoreCase(dto.getEstado())
                && !"Aprobada".equalsIgnoreCase(estadoAnterior)) {

            String urlReducir = "http://localhost:8094/api/stock-libros/reducir";
            String urlAñadir  = "http://localhost:8094/api/stock-libros/añadir";

            for (ItemTransferencia item : existente.getItems()) {
            // Crear request para este ítem
                TransferenciaStockRequest request = new TransferenciaStockRequest();
                request.setIdLibro(item.getIdLibro());
                request.setCantidad(item.getCantidad());

            // Reducir en sucursal origen
                request.setIdSucursalOrigen(existente.getIdSucursalOrigen());
                restTemplate.postForObject(urlReducir, request, String.class);

            // Añadir en sucursal destino
                request.setIdSucursalDestino(existente.getIdSucursalDestino());
                restTemplate.postForObject(urlAñadir, request, String.class);
            }

        // Crear envío (US-ENV-07)
            String urlEnvio = "http://localhost:8084/api/envios";
            EnvioDTO envioDTO = new EnvioDTO();
            envioDTO.setIdTransferencia(existente.getId());
            envioDTO.setDireccionOrigen("Dirección origen " + existente.getIdSucursalOrigen());
            envioDTO.setDireccionDestino("Dirección destino " + existente.getIdSucursalDestino());
            envioDTO.setFechaSolicitud(new Date());
            restTemplate.postForObject(urlEnvio, envioDTO, EnvioDTO.class);
        }

        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<TransferenciaDTO> listarPorSucursal(Long idSucursal) {
        return repository.findByIdSucursalOrigenOrIdSucursalDestino(idSucursal, idSucursal)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private void transferirStock(Long idOrigen, Long idDestino, Long idLibro, int cantidad) {
        String url = "http://localhost:8094/api/stock-libros/transferir";

        TransferenciaStockRequest request = new TransferenciaStockRequest();
        request.setIdSucursalOrigen(idOrigen);
        request.setIdSucursalDestino(idDestino);
        request.setIdLibro(idLibro);
        request.setCantidad(cantidad);

        try {
            restTemplate.postForObject(url, request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al transferir stock: " + e.getMessage());
        }
    }
}
