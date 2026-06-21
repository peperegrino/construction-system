package felipe.org.global_materials.controller;


import felipe.org.global_materials.dtos.response.FluxoCaixaDiarioResponseDTO;
import felipe.org.global_materials.dtos.response.ProdutoMaisVendidoResponseDTO;
import felipe.org.global_materials.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/relatorios")
@PreAuthorize("hasRole('ADMIN')")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<List<ProdutoMaisVendidoResponseDTO>> produtosMaisVendidos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(relatorioService.produtosMaisVendidos(inicio, fim));
    }

    @GetMapping("/fluxo-caixa")
    public ResponseEntity<List<FluxoCaixaDiarioResponseDTO>> fluxoCaixaDiario(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(relatorioService.fluxoCaixaDiario(inicio, fim));
    }

    @GetMapping("/fluxo-caixa/hoje")
    public ResponseEntity<FluxoCaixaDiarioResponseDTO> fluxoCaixaDeHoje() {
        return ResponseEntity.ok(relatorioService.fluxoCaixaDeHoje());
    }
}
