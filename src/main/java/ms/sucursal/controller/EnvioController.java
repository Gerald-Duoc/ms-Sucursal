package ms.sucursal.controller;

import lombok.RequiredArgsConstructor;
import ms.sucursal.model.EnvioDTO;
import ms.sucursal.service.EnvioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService service;

    @PostMapping
    public ResponseEntity<EnvioDTO> crear(@RequestBody EnvioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
}