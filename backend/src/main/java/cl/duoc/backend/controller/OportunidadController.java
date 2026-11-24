package cl.duoc.backend.controller;

import cl.duoc.backend.model.OportunidadModel;
import cl.duoc.backend.repository.OportunidadSpringRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
@CrossOrigin(origins = "*")
public class OportunidadController {

    private final OportunidadSpringRepository repository;

    public OportunidadController(OportunidadSpringRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<OportunidadModel> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OportunidadModel> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OportunidadModel crear(@RequestBody OportunidadModel oportunidad) {
        long now = System.currentTimeMillis();
        if (oportunidad.getCreadoEn() == null) oportunidad.setCreadoEn(now);
        oportunidad.setActualizadoEn(now);
        return repository.save(oportunidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OportunidadModel> actualizar(@PathVariable Long id, @RequestBody OportunidadModel oportunidad) {
        return repository.findById(id).map(existente -> {
            existente.setNombre(oportunidad.getNombre());
            existente.setTipo(oportunidad.getTipo());
            existente.setDescripcion(oportunidad.getDescripcion());
            existente.setFuente(oportunidad.getFuente());
            existente.setPrioridad(oportunidad.getPrioridad());
            existente.setPotencial(oportunidad.getPotencial());
            existente.setEstado(oportunidad.getEstado());
            existente.setObservaciones(oportunidad.getObservaciones());
            existente.setFotoPath(oportunidad.getFotoPath());
            existente.setLatitud(oportunidad.getLatitud());
            existente.setLongitud(oportunidad.getLongitud());
            existente.setActualizadoEn(System.currentTimeMillis());
            return ResponseEntity.ok(repository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
