package felipe.org.global_materials.service;

import felipe.org.global_materials.dtos.response.FluxoCaixaDiarioResponseDTO;
import felipe.org.global_materials.dtos.response.ProdutoMaisVendidoResponseDTO;
import felipe.org.global_materials.entities.Venda;
import felipe.org.global_materials.repository.ItemVendaRepository;
import felipe.org.global_materials.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final ItemVendaRepository itemVendaRepository;
    private final VendaRepository vendaRepository;

    public RelatorioService(ItemVendaRepository itemVendaRepository, VendaRepository vendaRepository) {
        this.itemVendaRepository = itemVendaRepository;
        this.vendaRepository = vendaRepository;
    }

    public List<ProdutoMaisVendidoResponseDTO> produtosMaisVendidos(LocalDateTime inicio, LocalDateTime fim) {
        return itemVendaRepository.findProdutosMaisVendidos(inicio, fim);
    }

    /** Relatorio de fluxo de caixa diario: total vendido e numero de vendas, agrupado por dia. */
    public List<FluxoCaixaDiarioResponseDTO> fluxoCaixaDiario(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = (inicio != null && fim != null)
                ? vendaRepository.findByDataVendaBetween(inicio, fim)
                : vendaRepository.findAll();

        Map<LocalDate, List<Venda>> agrupadoPorDia = vendas.stream()
                .collect(Collectors.groupingBy(v -> v.getDataVenda().toLocalDate()));

        return agrupadoPorDia.entrySet().stream()
                .map(entry -> FluxoCaixaDiarioResponseDTO.builder()
                        .data(entry.getKey())
                        .quantidadeVendas((long) entry.getValue().size())
                        .valorTotal(entry.getValue().stream()
                                .mapToDouble(Venda::getValorTotal)
                                .sum())
                        .build())
                .sorted(Comparator.comparing(FluxoCaixaDiarioResponseDTO::getData).reversed())
                .collect(Collectors.toList());
    }

    public FluxoCaixaDiarioResponseDTO fluxoCaixaDeHoje() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime fimDoDia = hoje.atTime(23, 59, 59);

        List<Venda> vendasDeHoje = vendaRepository.findByDataVendaBetween(inicioDoDia, fimDoDia);

        double total = vendasDeHoje.stream()
                .mapToDouble(Venda::getValorTotal)
                .sum();

        return FluxoCaixaDiarioResponseDTO.builder()
                .data(hoje)
                .quantidadeVendas((long) vendasDeHoje.size())
                .valorTotal(total)
                .build();
    }
}