package felipe.org.global_materials.controller;

import felipe.org.global_materials.dtos.request.LoginRequestDTO;
import felipe.org.global_materials.dtos.request.UsuarioRequestDTO;
import felipe.org.global_materials.dtos.response.LoginResponseDTO;
import felipe.org.global_materials.dtos.response.UsuarioResponseDTO;
import felipe.org.global_materials.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authenticationService.login(dto));
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO response = authenticationService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}