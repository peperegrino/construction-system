package felipe.org.global_materials.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Long usuarioId;
    private String usuarioNome;
    private LocalDateTime dataVenda;
    private double valorTotal;
    private List<ItemVendaResponseDTO> itens;
}
