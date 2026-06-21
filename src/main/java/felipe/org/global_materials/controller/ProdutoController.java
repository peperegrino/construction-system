package felipe.org.global_materials.controller;


import felipe.org.global_materials.dtos.request.ProdutoRequestDTO;
import felipe.org.global_materials.dtos.response.ProdutoResponseDTO;
import felipe.org.global_materials.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodos(
            @RequestParam(required = false) String descricao) {
        if (descricao != null && !descricao.isBlank()) {
            return ResponseEntity.ok(produtoService.buscarPorDescricao(descricao));
        }
        return ResponseEntity.ok(produtoService.buscarTodos());
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarComEstoqueBaixo() {
        return ResponseEntity.ok(produtoService.buscarComEstoqueBaixo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

   @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(produtoService.buscarPorCodigo(codigo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
