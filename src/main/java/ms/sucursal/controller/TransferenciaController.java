package ms.sucursal.controller;

import lombok.RequiredArgsConstructor;
import ms.sucursal.model.TransferenciaDTO;
import ms.sucursal.service.TransferenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService service;

    @GetMapping
    public ResponseEntity<List<TransferenciaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> buscar(@PathVariable Long id) {
        return service.buscar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransferenciaDTO> crear(@RequestBody TransferenciaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> actualizar(@PathVariable Long id,
                                                       @RequestBody TransferenciaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<TransferenciaDTO>> listarPorSucursal(@PathVariable Long idSucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(idSucursal));
    }
}
