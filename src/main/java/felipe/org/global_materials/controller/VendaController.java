package felipe.org.global_materials.controller;

import felipe.org.global_materials.dtos.request.VendaRequestDTO;
import felipe.org.global_materials.dtos.response.VendaResponseDTO;
import felipe.org.global_materials.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponseDTO> registrar(@Valid @RequestBody VendaRequestDTO dto) {
        return ResponseEntity.ok(vendaService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> buscarTodas() {
        return ResponseEntity.ok(vendaService.buscarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vendaService.buscarPorCliente(clienteId));
    }


}