package felipe.org.global_materials.service;

import felipe.org.global_materials.dtos.request.ItemVendaRequestDTO;
import felipe.org.global_materials.dtos.request.VendaRequestDTO;
import felipe.org.global_materials.dtos.response.VendaResponseDTO;
import felipe.org.global_materials.entities.*;
import felipe.org.global_materials.repository.ClienteRepository;
import felipe.org.global_materials.repository.ProdutoRepository;
import felipe.org.global_materials.repository.UsuarioRepository;
import felipe.org.global_materials.repository.VendaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public VendaService(VendaRepository vendaRepository,
                        ClienteRepository clienteRepository,
                        ProdutoRepository produtoRepository,
                        UsuarioRepository usuarioRepository,
                        MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @Transactional
    public VendaResponseDTO registrar(VendaRequestDTO dto) {
        Usuario usuarioLogado = obterUsuarioLogado();

        Cliente cliente = null;
        if (dto.getClienteId() != null) {
            cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + dto.getClienteId()));
        }

        Venda venda = Venda.builder()
                .cliente(cliente)
                .usuario(usuarioLogado)
                .valorTotal(0.0)
                .build();

        double valorTotalVenda = 0.0;

        for (ItemVendaRequestDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDto.getProdutoId()));

            movimentacaoEstoqueService.registrarSaidaPorVenda(produto, itemDto.getQuantidade());

            double subtotal = produto.getPrecoVenda() * itemDto.getQuantidade();

            ItemVenda item = ItemVenda.builder()
                    .produto(produto)
                    .quantidade(itemDto.getQuantidade())
                    .precoUnitario(produto.getPrecoVenda())
                    .subtotal(subtotal)
                    .build();

            venda.adicionarItem(item);
            valorTotalVenda = valorTotalVenda + subtotal;
        }

        venda.setValorTotal(valorTotalVenda);
        Venda salva = vendaRepository.save(venda);

        return converterParaDTO(salva);
    }

    public List<VendaResponseDTO> buscarTodas() {
        return vendaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public VendaResponseDTO buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com ID: " + id));
        return converterParaDTO(venda);
    }

    public List<VendaResponseDTO> buscarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private Usuario obterUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado: " + email));
    }

    private VendaResponseDTO converterParaDTO(Venda venda) {
        return VendaResponseDTO.builder()
                .id(venda.getId())
                .valorTotal(venda.getValorTotal())
                .clienteNome(venda.getCliente() != null ? venda.getCliente().getNome() : "Consumidor Final")
                .usuarioNome(venda.getUsuario() != null ? venda.getUsuario().getNome() : null)
                .build();
    }
}