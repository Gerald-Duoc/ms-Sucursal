package ms.sucursal;

import com.fasterxml.jackson.databind.ObjectMapper;
import ms.sucursal.controller.SucursalController;
import ms.sucursal.model.SucursalDTO;
import ms.sucursal.service.SucursalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SucursalService service;

    private SucursalDTO createDTO(Long id) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(id);
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
    void listar_ShouldReturnList() throws Exception {
        when(service.listar()).thenReturn(List.of(createDTO(1L), createDTO(2L)));

        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void buscar_Found_ShouldReturn200() throws Exception {
        when(service.buscar(1L)).thenReturn(Optional.of(createDTO(1L)));

        mockMvc.perform(get("/api/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscar_NotFound_ShouldReturn404() throws Exception {
        when(service.buscar(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sucursales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_ShouldReturn201() throws Exception {
        SucursalDTO input = createDTO(null);
        SucursalDTO output = createDTO(1L);
        when(service.guardar(any(SucursalDTO.class))).thenReturn(output);

        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_ShouldReturn200() throws Exception {
        SucursalDTO dto = createDTO(1L);
        when(service.actualizar(eq(1L), any(SucursalDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/sucursales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminar_ShouldReturn204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/sucursales/1"))
                .andExpect(status().isNoContent());
    }
}
