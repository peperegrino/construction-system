package felipe.org.global_materials.service;


import felipe.org.global_materials.dtos.request.LoginRequestDTO;
import felipe.org.global_materials.dtos.request.UsuarioRequestDTO;
import felipe.org.global_materials.dtos.response.LoginResponseDTO;
import felipe.org.global_materials.dtos.response.UsuarioResponseDTO;
import felipe.org.global_materials.entities.Usuario;
import felipe.org.global_materials.repository.UsuarioRepository;
import felipe.org.global_materials.security.CustomUserDetailsService;
import felipe.org.global_materials.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    public AuthenticationService(UsuarioRepository usuarioRepository,
          PasswordEncoder passwordEncoder,
          AuthenticationManager authenticationManager,
          JwtService jwtService,
          CustomUserDetailsService userDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken((UserDetails) usuario);


        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();
    }
}

