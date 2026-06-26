package ms.sucursal.service;

import lombok.RequiredArgsConstructor;
import ms.sucursal.model.Sucursal;
import ms.sucursal.model.SucursalDTO;
import ms.sucursal.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SucursalService {

    private final SucursalRepository repository;

    private SucursalDTO toDTO(Sucursal s) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(s.getId());
        dto.setIdAdminGeneral(s.getIdAdminGeneral());
        dto.setIdGerenteSede(s.getIdGerenteSede());
        dto.setNombre(s.getNombre());
        dto.setDireccion(s.getDireccion());
        dto.setFechaInicio(s.getFechaInicio());
        dto.setTelefono(s.getTelefono());
        dto.setEmail(s.getEmail());
        dto.setEstado(s.getEstado());
        return dto;
    }

    private Sucursal toEntity(SucursalDTO dto) {
        Sucursal s = new Sucursal();
        s.setId(dto.getId());
        s.setIdAdminGeneral(dto.getIdAdminGeneral());
        s.setIdGerenteSede(dto.getIdGerenteSede());
        s.setNombre(dto.getNombre());
        s.setDireccion(dto.getDireccion());
        s.setFechaInicio(dto.getFechaInicio());
        s.setTelefono(dto.getTelefono());
        s.setEmail(dto.getEmail());
        s.setEstado(dto.getEstado());
        return s;
    }

    public List<SucursalDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<SucursalDTO> buscar(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public SucursalDTO guardar(SucursalDTO dto) {
        Sucursal s = toEntity(dto);
        s.setId(null);
        return toDTO(repository.save(s));
    }

    public SucursalDTO actualizar(Long id, SucursalDTO dto) {
        Sucursal existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
        existente.setIdAdminGeneral(dto.getIdAdminGeneral());
        existente.setIdGerenteSede(dto.getIdGerenteSede());
        existente.setNombre(dto.getNombre());
        existente.setDireccion(dto.getDireccion());
        existente.setFechaInicio(dto.getFechaInicio());
        existente.setTelefono(dto.getTelefono());
        existente.setEmail(dto.getEmail());
        existente.setEstado(dto.getEstado());
        // ---------- FUTURA CONEXIÓN CON REGISTRO USUARIOS (US-SUC-04) ----------
        // Si cambia idGerenteSede, validar que el nuevo gerente exista y tenga el rol adecuado.
        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        // En el futuro, podría desactivarse en vez de eliminar (estado = false)
        repository.deleteById(id);
    }
}