package ms.sucursal.service;

import ms.sucursal.model.Envio;
import ms.sucursal.model.EnvioDTO;
import ms.sucursal.repository.EnvioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository repository;

    @InjectMocks
    private EnvioService service;

    private EnvioDTO createDTO() {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdTransferencia(1L);
        dto.setDireccionOrigen("Origen");
        dto.setDireccionDestino("Destino");
        return dto;
    }

    @Test
    void guardar_deberiaRetornarDTO() {
        EnvioDTO input = createDTO();
        Envio saved = new Envio();
        saved.setId(1L);
        saved.setIdTransferencia(1L);
        saved.setDireccionOrigen("Origen");
        saved.setDireccionDestino("Destino");

        when(repository.save(any(Envio.class))).thenReturn(saved);

        EnvioDTO result = service.guardar(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Origen", result.getDireccionOrigen());
        verify(repository).save(any(Envio.class));
    }

    @Test
    void guardar_deberiaLimpiarIdAntesDeGuardar() {
        EnvioDTO input = createDTO();
        input.setId(99L);

        Envio saved = new Envio();
        saved.setId(5L);
        saved.setIdTransferencia(1L);

        when(repository.save(any(Envio.class))).thenReturn(saved);

        EnvioDTO result = service.guardar(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        verify(repository).save(any(Envio.class));
    }
}
