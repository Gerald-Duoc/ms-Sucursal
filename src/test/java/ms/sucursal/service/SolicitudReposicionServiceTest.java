package ms.sucursal.service;

import ms.sucursal.model.SolicitudReposicion;
import ms.sucursal.model.SolicitudReposicionDTO;
import ms.sucursal.repository.SolicitudReposicionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudReposicionServiceTest {

    @Mock
    private SolicitudReposicionRepository repository;

    @InjectMocks
    private SolicitudReposicionService service;

    private SolicitudReposicion createEntity(Long id) {
        SolicitudReposicion s = new SolicitudReposicion();
        s.setId(id);
        s.setIdSucursal(1L);
        s.setEstado("Pendiente");
        s.setFechaSolicitud(new Date());
        return s;
    }

    private SolicitudReposicionDTO createDTO() {
        SolicitudReposicionDTO dto = new SolicitudReposicionDTO();
        dto.setIdSucursal(1L);
        dto.setEstado("Pendiente");
        dto.setFechaSolicitud(new Date());
        return dto;
    }

    @Test
    void listar_deberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(createEntity(1L), createEntity(2L)));

        List<SolicitudReposicionDTO> result = service.listar();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void listar_vacia_deberiaRetornarListaVacia() {
        when(repository.findAll()).thenReturn(List.of());

        List<SolicitudReposicionDTO> result = service.listar();

        assertTrue(result.isEmpty());
    }

    @Test
    void buscar_encontrado_deberiaRetornarDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(createEntity(1L)));

        Optional<SolicitudReposicionDTO> result = service.buscar(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdSucursal());
    }

    @Test
    void buscar_noEncontrado_deberiaRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<SolicitudReposicionDTO> result = service.buscar(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void guardar_deberiaRetornarDTO() {
        SolicitudReposicionDTO input = createDTO();
        SolicitudReposicion saved = createEntity(1L);

        when(repository.save(any(SolicitudReposicion.class))).thenReturn(saved);

        SolicitudReposicionDTO result = service.guardar(input);

        assertNotNull(result);
        assertEquals("Pendiente", result.getEstado());
        verify(repository).save(any(SolicitudReposicion.class));
    }

    @Test
    void actualizar_deberiaActualizarCampos() {
        SolicitudReposicion existing = createEntity(1L);
        SolicitudReposicionDTO input = createDTO();
        input.setEstado("Aprobada");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(SolicitudReposicion.class))).thenReturn(existing);

        SolicitudReposicionDTO result = service.actualizar(1L, input);

        assertNotNull(result);
        verify(repository).save(existing);
    }

    @Test
    void actualizar_noEncontrado_deberiaLanzarExcepcion() {
        SolicitudReposicionDTO input = createDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.actualizar(99L, input));
        assertEquals("Solicitud no encontrada", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_deberiaLlamarDeleteById() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void listarPorSucursal_deberiaRetornarFiltradas() {
        when(repository.findByIdSucursal(1L)).thenReturn(List.of(createEntity(1L)));

        List<SolicitudReposicionDTO> result = service.listarPorSucursal(1L);

        assertEquals(1, result.size());
        verify(repository).findByIdSucursal(1L);
    }
}
