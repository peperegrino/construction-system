package felipe.org.global_materials.service;

import org.springframework.transaction.annotation.Transactional;
import felipe.org.global_materials.dtos.request.ProdutoRequestDTO;
import felipe.org.global_materials.dtos.response.ProdutoResponseDTO;
import felipe.org.global_materials.entities.Fornecedor;
import felipe.org.global_materials.entities.Produto;
import felipe.org.global_materials.repository.FornecedorRepository;
import felipe.org.global_materials.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }


    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
        if (produtoRepository.existsByCodigo(dto.getCodigo())) {
            throw new RuntimeException("Já existe um produto cadastrado com o código: " + dto.getCodigo());
        }
        Fornecedor fornecedor = buscarFornecedorSeInformado(dto.getFornecedorId());

        Produto produto = Produto.builder()
                .codigo(dto.getCodigo())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .precoCusto(dto.getPrecoCusto())
                .precoVenda(dto.getPrecoVenda())
                .quantidadeEstoque(dto.getQuantidadeEstoque() != null ? dto.getQuantidadeEstoque() : 0)
                .estoqueMinimo(dto.getEstoqueMinimo()        != null ? dto.getEstoqueMinimo()        : 0)
                .imagemUrl(dto.getImagemUrl())
                .fornecedor(fornecedor)
                .build();

        return converterParaDTO(produtoRepository.save(produto));
    }


    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarTodos() {

        List<Produto> produtos = produtoRepository.findAll();

        System.out.println("=== PRODUTOS ENCONTRADOS ===");
        System.out.println(produtos.size());

        return produtos.stream()
                .map(produto -> {
                    System.out.println(
                            "Convertendo produto ID: " + produto.getId()
                    );
                    return converterParaDTO(produto);
                })
                .toList();
    }

    public List<ProdutoResponseDTO> buscarPorDescricao(String descricao) {
        return produtoRepository.findByDescricaoContainingIgnoreCase(descricao).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> buscarComEstoqueBaixo() {
        return produtoRepository.findProdutosComEstoqueBaixo().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        return converterParaDTO(produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id)));
    }

    public ProdutoResponseDTO buscarPorCodigo(String codigo) {
        return converterParaDTO(produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o código: " + codigo)));
    }

    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

        if (!produto.getCodigo().equals(dto.getCodigo()) && produtoRepository.existsByCodigo(dto.getCodigo())) {
            throw new RuntimeException("Já existe um produto cadastrado com o código: " + dto.getCodigo());
        }

        Fornecedor fornecedor = buscarFornecedorSeInformado(dto.getFornecedorId());

        produto.setCodigo(dto.getCodigo());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPrecoCusto(dto.getPrecoCusto());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        produto.setEstoqueMinimo(dto.getEstoqueMinimo());
        produto.setImagemUrl(dto.getImagemUrl());
        produto.setFornecedor(fornecedor);

        return converterParaDTO(produtoRepository.save(produto));
    }


  public void excluir(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    private Fornecedor buscarFornecedorSeInformado(Long fornecedorId) {
        if (fornecedorId == null) return null;
        return fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com ID: " + fornecedorId));
    }

    private ProdutoResponseDTO converterParaDTO(Produto produto) {
        double margem = produto.getPrecoCusto() > 0
                ? ((produto.getPrecoVenda() - produto.getPrecoCusto()) / produto.getPrecoCusto()) * 100
                : 0;

        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .codigo(produto.getCodigo())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .precoCusto(produto.getPrecoCusto())
                .precoVenda(produto.getPrecoVenda())
                .margemLucroPercentual(Math.round(margem * 100.0) / 100.0)
                .quantidadeEstoque(produto.getQuantidadeEstoque())
                .estoqueMinimo(produto.getEstoqueMinimo())
                .estoqueBaixo(produto.isEstoqueBaixo())
                .imagemUrl(produto.getImagemUrl())
                .ativo(produto.getAtivo())
                .fornecedorId(produto.getFornecedor() != null ? produto.getFornecedor().getId()   : null)
                .fornecedorNome(produto.getFornecedor() != null ? produto.getFornecedor().getNome() : null)
                .build();
    }
}