package felipe.org.global_materials.controller;

import felipe.org.global_materials.dtos.request.FornecedorRequestDTO;
import felipe.org.global_materials.dtos.response.FornecedorResponseDTO;
import felipe.org.global_materials.dtos.response.ProdutoResponseDTO;
import felipe.org.global_materials.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FornecedorResponseDTO> cadastrar(@Valid @RequestBody FornecedorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(fornecedorService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @GetMapping("/{id}/produtos")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarProdutosPorFornecedor(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarProdutosPorFornecedor(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FornecedorResponseDTO> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody FornecedorRequestDTO dto) {
        return ResponseEntity.ok(fornecedorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        fornecedorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}