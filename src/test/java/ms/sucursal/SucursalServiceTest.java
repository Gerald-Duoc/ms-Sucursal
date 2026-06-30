package ms.sucursal;

import ms.sucursal.model.Sucursal;
import ms.sucursal.model.SucursalDTO;
import ms.sucursal.repository.SucursalRepository;
import ms.sucursal.service.SucursalService;
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
class SucursalServiceTest {

    @Mock
    private SucursalRepository repository;

    @InjectMocks
    private SucursalService service;

    private Sucursal createEntity(Long id) {
        Sucursal s = new Sucursal();
        s.setId(id);
        s.setIdAdminGeneral(1L);
        s.setIdGerenteSede(2L);
        s.setNombre("Sucursal Test");
        s.setDireccion("Av. Siempre Viva 123");
        s.setFechaInicio(new Date());
        s.setTelefono("123456789");
        s.setEmail("test@test.com");
        s.setEstado(true);
        return s;
    }

    private SucursalDTO createDTO() {
        SucursalDTO dto = new SucursalDTO();
        dto.setIdAdminGeneral(1L);
        dto.setIdGerenteSede(2L);
        dto.setNombre("Sucursal Test");
        dto.setDireccion("Av. Siempre Viva 123");
        dto.setFechaInicio(new Date());
        dto.setTelefono("123456789");
        dto.setEmail("test@test.com");
        dto.setEstado(true);
        return dto;
    }

    @Test
    void testGuardar() {
        SucursalDTO dto = createDTO();
        Sucursal entity = createEntity(null);
        Sucursal savedEntity = createEntity(1L);

        when(repository.save(any(Sucursal.class))).thenReturn(savedEntity);

        SucursalDTO result = service.guardar(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(dto.getNombre(), result.getNombre());
        verify(repository).save(any(Sucursal.class));
    }

    @Test
    void testListar() {
        Sucursal s1 = createEntity(1L);
        Sucursal s2 = createEntity(2L);
        s2.setNombre("Sucursal 2");
        when(repository.findAll()).thenReturn(List.of(s1, s2));

        List<SucursalDTO> result = service.listar();

        assertEquals(2, result.size());
        assertEquals("Sucursal Test", result.get(0).getNombre());
        assertEquals("Sucursal 2", result.get(1).getNombre());
    }

    @Test
    void testBuscar_Found() {
        Sucursal entity = createEntity(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<SucursalDTO> result = service.buscar(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Sucursal Test", result.get().getNombre());
    }

    @Test
    void testBuscar_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<SucursalDTO> result = service.buscar(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testActualizar() {
        Sucursal existing = createEntity(1L);
        SucursalDTO dto = createDTO();
        dto.setNombre("Sucursal Modificada");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Sucursal.class))).thenReturn(existing);

        SucursalDTO result = service.actualizar(1L, dto);

        assertNotNull(result);
        assertEquals("Sucursal Modificada", result.getNombre());
        verify(repository).save(existing);
    }

    @Test
    void testActualizar_NotFound() {
        SucursalDTO dto = createDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.actualizar(99L, dto));
        assertEquals("Sucursal no encontrada", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testEliminar() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }
}
