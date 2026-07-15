package ms.sucursal.service;

import ms.sucursal.model.Transferencia;
import ms.sucursal.model.TransferenciaDTO;
import ms.sucursal.model.ItemTransferencia;
import ms.sucursal.repository.TransferenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransferenciaService service;

    private Transferencia createEntity(Long id) {
        Transferencia t = new Transferencia();
        t.setId(id);
        t.setIdSucursalOrigen(1L);
        t.setIdSucursalDestino(2L);
        t.setEstado("Pendiente");
        t.setFechaSolicitud(new Date());
        t.setIdEmpleadoRecepcion(10L);
        t.setItems(new ArrayList<>());
        return t;
    }

    private TransferenciaDTO createDTO() {
        TransferenciaDTO dto = new TransferenciaDTO();
        dto.setIdSucursalOrigen(1L);
        dto.setIdSucursalDestino(2L);
        dto.setEstado("Pendiente");
        dto.setFechaSolicitud(new Date());
        dto.setIdEmpleadoRecepcion(10L);
        dto.setItems(new ArrayList<>());
        return dto;
    }

    @Test
    void listar_deberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(createEntity(1L), createEntity(2L)));

        List<TransferenciaDTO> result = service.listar();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void listar_vacia_deberiaRetornarListaVacia() {
        when(repository.findAll()).thenReturn(List.of());

        List<TransferenciaDTO> result = service.listar();

        assertTrue(result.isEmpty());
    }

    @Test
    void buscar_encontrado_deberiaRetornarDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(createEntity(1L)));

        Optional<TransferenciaDTO> result = service.buscar(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdSucursalOrigen());
    }

    @Test
    void buscar_noEncontrado_deberiaRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<TransferenciaDTO> result = service.buscar(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void guardar_deberiaRetornarDTO() {
        TransferenciaDTO input = createDTO();
        Transferencia saved = createEntity(1L);

        when(repository.save(any(Transferencia.class))).thenReturn(saved);

        TransferenciaDTO result = service.guardar(input);

        assertNotNull(result);
        assertEquals("Pendiente", result.getEstado());
        verify(repository).save(any(Transferencia.class));
    }

    @Test
    void actualizar_estadoNoAprobada_deberiaGuardarSinLlamarRestTemplate() {
        Transferencia existing = createEntity(1L);
        existing.setEstado("Pendiente");
        TransferenciaDTO input = createDTO();
        input.setEstado("En tránsito");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Transferencia.class))).thenReturn(existing);

        TransferenciaDTO result = service.actualizar(1L, input);

        assertNotNull(result);
        verify(repository).save(existing);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void actualizar_noEncontrado_deberiaLanzarExcepcion() {
        TransferenciaDTO input = createDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.actualizar(99L, input));
        assertEquals("Transferencia no encontrada", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_deberiaLlamarDeleteById() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void listarPorSucursal_deberiaRetornarFiltradas() {
        when(repository.findByIdSucursalOrigenOrIdSucursalDestino(1L, 1L))
                .thenReturn(List.of(createEntity(1L)));

        List<TransferenciaDTO> result = service.listarPorSucursal(1L);

        assertEquals(1, result.size());
        verify(repository).findByIdSucursalOrigenOrIdSucursalDestino(1L, 1L);
    }

    @Test
    void actualizar_aprobarConItems_deberiaTransferirStock() {
        Transferencia existing = createEntity(1L);
        existing.setEstado("Pendiente");
        ItemTransferencia item = new ItemTransferencia();
        item.setId(1L);
        item.setIdLibro(10L);
        item.setCantidad(5);
        existing.setItems(new ArrayList<>(List.of(item)));

        TransferenciaDTO input = createDTO();
        input.setEstado("Aprobada");
        input.setItems(new ArrayList<>(List.of(item)));

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Transferencia.class))).thenReturn(existing);
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn("ok");

        TransferenciaDTO result = service.actualizar(1L, input);

        assertNotNull(result);
        verify(repository).save(existing);
        verify(restTemplate, times(2)).postForObject(anyString(), any(), eq(String.class));
    }
}
