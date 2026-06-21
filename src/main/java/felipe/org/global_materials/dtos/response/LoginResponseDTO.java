package felipe.org.global_materials.dtos.response;


import felipe.org.global_materials.entities.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String tipo;
    private String nome;
    private String email;
    private RoleType role;
}
