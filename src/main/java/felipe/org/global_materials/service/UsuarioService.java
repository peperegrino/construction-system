package felipe.org.global_materials.service;

import felipe.org.global_materials.dtos.response.UsuarioResponseDTO;
import felipe.org.global_materials.entities.Usuario;
import felipe.org.global_materials.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponseDTO> buscarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> UsuarioResponseDTO.builder()
                        .id(usuario.getId())
                        .nome(usuario.getNome())
                        .email(usuario.getEmail())
                        .role(usuario.getRole())
                        .ativo(usuario.getAtivo())
                        .build())
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = buscarEntidadePorId(id);

        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .ativo(usuario.getAtivo())
                .build();
    }

    public void inativar(Long id) {
        Usuario usuario = buscarEntidadePorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void reativar(Long id) {
        Usuario usuario = buscarEntidadePorId(id);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    private Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }
}