package felipe.org.global_materials.security;


import felipe.org.global_materials.entities.Usuario;
import felipe.org.global_materials.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado com o e-mail: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()))
                .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                .build();
    }
}
