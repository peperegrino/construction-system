package felipe.org.global_materials.service;

import felipe.org.global_materials.dtos.request.LoginRequestDTO;
import felipe.org.global_materials.dtos.request.UsuarioRequestDTO;
import felipe.org.global_materials.dtos.response.LoginResponseDTO;
import felipe.org.global_materials.dtos.response.UsuarioResponseDTO;
import felipe.org.global_materials.entities.Usuario;
import felipe.org.global_materials.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO registrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(dto.getRole())
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        return UsuarioResponseDTO.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .email(salvo.getEmail())
                .role(salvo.getRole())
                .build();
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

         return converterParaDTO(usuario);
    }


    private LoginResponseDTO converterParaDTO(Usuario usuario) {
        return LoginResponseDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();
    }
}