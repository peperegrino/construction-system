package felipe.org.global_materials.dtos.response;


import felipe.org.global_materials.entities.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private RoleType role;
    private Boolean ativo;
    private LocalDateTime criadoEm;
}
