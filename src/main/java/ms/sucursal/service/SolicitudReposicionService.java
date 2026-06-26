package ms.sucursal.service;

import lombok.RequiredArgsConstructor;
import ms.sucursal.model.SolicitudReposicion;
import ms.sucursal.model.SolicitudReposicionDTO;
import ms.sucursal.repository.SolicitudReposicionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudReposicionService {

    private final SolicitudReposicionRepository repository;

    private SolicitudReposicionDTO toDTO(SolicitudReposicion s) {
        SolicitudReposicionDTO dto = new SolicitudReposicionDTO();
        dto.setId(s.getId());
        dto.setIdSucursal(s.getIdSucursal());
        dto.setEstado(s.getEstado());
        dto.setFechaSolicitud(s.getFechaSolicitud());
        return dto;
    }

    private SolicitudReposicion toEntity(SolicitudReposicionDTO dto) {
        SolicitudReposicion s = new SolicitudReposicion();
        s.setId(dto.getId());
        s.setIdSucursal(dto.getIdSucursal());
        s.setEstado(dto.getEstado());
        s.setFechaSolicitud(dto.getFechaSolicitud());
        return s;
    }

    public List<SolicitudReposicionDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<SolicitudReposicionDTO> buscar(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public SolicitudReposicionDTO guardar(SolicitudReposicionDTO dto) {
        SolicitudReposicion s = toEntity(dto);
        s.setId(null);
        return toDTO(repository.save(s));
    }

    public SolicitudReposicionDTO actualizar(Long id, SolicitudReposicionDTO dto) {
        SolicitudReposicion existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        existente.setIdSucursal(dto.getIdSucursal());
        existente.setEstado(dto.getEstado());
        existente.setFechaSolicitud(dto.getFechaSolicitud());
        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<SolicitudReposicionDTO> listarPorSucursal(Long idSucursal) {
        return repository.findByIdSucursal(idSucursal)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}