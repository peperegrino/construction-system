package felipe.org.global_materials.controller;

import felipe.org.global_materials.dtos.request.EntradaEstoqueRequestDTO;
import felipe.org.global_materials.dtos.response.MovimentacaoEstoqueResponseDTO;
import felipe.org.global_materials.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @PostMapping("/entrada")
    public ResponseEntity<MovimentacaoEstoqueResponseDTO> registrarEntrada(
            @Valid @RequestBody EntradaEstoqueRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoEstoqueService.registrarEntrada(dto));
    }



    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> buscarTodas() {
        return ResponseEntity.ok(movimentacaoEstoqueService.buscarTodas());
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> buscarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(movimentacaoEstoqueService.buscarPorProduto(produtoId));
    }
}