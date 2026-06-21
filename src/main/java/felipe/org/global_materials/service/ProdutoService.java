package felipe.org.global_materials.service;

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
                .precoVenda(dto.getPrecoVenda())
                .quantidadeEstoque(dto.getQuantidadeEstoque() != null ? dto.getQuantidadeEstoque() : 0)
                .estoqueMinimo(dto.getEstoqueMinimo() != null ? dto.getEstoqueMinimo() : 0)
                .fornecedor(fornecedor)
                .build();

        Produto salvo = produtoRepository.save(produto);
        return converterParaDTO(salvo);
    }

    public List<ProdutoResponseDTO> buscarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
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
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        return converterParaDTO(produto);
    }

    public ProdutoResponseDTO buscarPorCodigo(String codigo) {
        Produto produto = produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o código: " + codigo));
        return converterParaDTO(produto);
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
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setEstoqueMinimo(dto.getEstoqueMinimo());
        produto.setFornecedor(fornecedor);

        Produto atualizado = produtoRepository.save(produto);
        return converterParaDTO(atualizado);
    }

    public void excluir(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    private Fornecedor buscarFornecedorSeInformado(Long fornecedorId) {
        if (fornecedorId == null) {
            return null;
        }
        return fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com ID: " + fornecedorId));
    }

    private ProdutoResponseDTO converterParaDTO(Produto produto) {
        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .codigo(produto.getCodigo())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .precoVenda(produto.getPrecoVenda())
                .quantidadeEstoque(produto.getQuantidadeEstoque())
                .estoqueMinimo(produto.getEstoqueMinimo())
                .ativo(produto.getAtivo())
                .fornecedorNome(produto.getFornecedor() != null ? produto.getFornecedor().getNome() : null)
                .build();
    }
}