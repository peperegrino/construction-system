package felipe.org.global_materials.service;

import felipe.org.global_materials.dtos.request.FornecedorRequestDTO;
import felipe.org.global_materials.dtos.response.FornecedorResponseDTO;
import felipe.org.global_materials.entities.Fornecedor;
import felipe.org.global_materials.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public FornecedorResponseDTO cadastrar(FornecedorRequestDTO dto) {
        Fornecedor fornecedor = Fornecedor.builder()
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .endereco(dto.getEndereco())
                .build();

        Fornecedor salvo = fornecedorRepository.save(fornecedor);
        return converterParaDTO(salvo);
    }

    public List<FornecedorResponseDTO> buscarTodos() {
        return fornecedorRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public FornecedorResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarEntidadePorId(id));
    }

    public FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = buscarEntidadePorId(id);

        fornecedor.setNome(dto.getNome());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setEndereco(dto.getEndereco());

        Fornecedor atualizado = fornecedorRepository.save(fornecedor);
        return converterParaDTO(atualizado);
    }

    public void excluir(Long id) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        fornecedor.setAtivo(false);
        fornecedorRepository.save(fornecedor);
    }

    private Fornecedor buscarEntidadePorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com ID: " + id));
    }

    private FornecedorResponseDTO converterParaDTO(Fornecedor fornecedor) {
        return FornecedorResponseDTO.builder()
                .id(fornecedor.getId())
                .nome(fornecedor.getNome())
                .telefone(fornecedor.getTelefone())
                .email(fornecedor.getEmail())
                .ativo(fornecedor.getAtivo())
                .build();
    }
}