package ms.sucursal.service;

import lombok.RequiredArgsConstructor;
import ms.sucursal.model.Envio;
import ms.sucursal.model.EnvioDTO;
import ms.sucursal.repository.EnvioRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository repository;

    private EnvioDTO toDTO(Envio e) {
        EnvioDTO dto = new EnvioDTO();
        dto.setId(e.getId());
        dto.setIdTransferencia(e.getIdTransferencia());
        dto.setDireccionOrigen(e.getDireccionOrigen());
        dto.setDireccionDestino(e.getDireccionDestino());
        dto.setFechaSolicitud(e.getFechaSolicitud());
        return dto;
    }

    private Envio toEntity(EnvioDTO dto) {
        Envio e = new Envio();
        e.setId(dto.getId());
        e.setIdTransferencia(dto.getIdTransferencia());
        e.setDireccionOrigen(dto.getDireccionOrigen());
        e.setDireccionDestino(dto.getDireccionDestino());
        e.setFechaSolicitud(dto.getFechaSolicitud());
        return e;
    }

    public EnvioDTO guardar(EnvioDTO dto) {
        Envio e = toEntity(dto);
        e.setId(null);
        return toDTO(repository.save(e));
    }
}