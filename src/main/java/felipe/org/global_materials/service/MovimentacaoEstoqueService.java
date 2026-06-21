package felipe.org.global_materials.service;

import felipe.org.global_materials.dtos.request.EntradaEstoqueRequestDTO;
import felipe.org.global_materials.dtos.response.MovimentacaoEstoqueResponseDTO;
import felipe.org.global_materials.entities.MovimentacaoEstoque;
import felipe.org.global_materials.entities.Produto;
import felipe.org.global_materials.entities.enums.TipoMovimentacao;
import felipe.org.global_materials.repository.MovimentacaoEstoqueRepository;
import felipe.org.global_materials.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoRepository,
                                      ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    public MovimentacaoEstoqueResponseDTO registrarEntrada(EntradaEstoqueRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + dto.getProdutoId()));

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + dto.getQuantidade());
        produtoRepository.save(produto);

        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .produto(produto)
                .tipo(TipoMovimentacao.ENTRADA)
                .quantidade(dto.getQuantidade())
                .observacao(dto.getObservacao())
                .build();

        MovimentacaoEstoque salva = movimentacaoRepository.save(movimentacao);
        return converterParaDTO(salva);
    }

    public void registrarSaidaPorVenda(Produto produto, Integer quantidade) {
        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente para o produto '" + produto.getNome() +
                    "'. Disponível: " + produto.getQuantidadeEstoque() + ", solicitado: " + quantidade);
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        produtoRepository.save(produto);

        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .produto(produto)
                .tipo(TipoMovimentacao.SAIDA)
                .quantidade(quantidade)
                .observacao("Baixa automática por venda")
                .build();

        movimentacaoRepository.save(movimentacao);
    }

    public List<MovimentacaoEstoqueResponseDTO> buscarTodas() {
        return movimentacaoRepository.findAllByOrderByDataMovimentacaoDesc().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoEstoqueResponseDTO> buscarPorProduto(Long produtoId) {
        return movimentacaoRepository.findByProdutoIdOrderByDataMovimentacaoDesc(produtoId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private MovimentacaoEstoqueResponseDTO converterParaDTO(MovimentacaoEstoque movimentacao) {
        return MovimentacaoEstoqueResponseDTO.builder()
                .id(movimentacao.getId())
                .produtoId(movimentacao.getProduto().getId())
                .produtoNome(movimentacao.getProduto().getNome())
                .tipoMovimentacao(movimentacao.getTipo())
                .quantidade(movimentacao.getQuantidade())
                .observacao(movimentacao.getObservacao())
                .dataMovimentacao(movimentacao.getDataMovimentacao())
                .build();
    }
}