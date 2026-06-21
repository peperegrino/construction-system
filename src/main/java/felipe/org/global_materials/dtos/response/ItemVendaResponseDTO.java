package felipe.org.global_materials.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemVendaResponseDTO {
    private Long id;
    private Long produtoId;
    private String produtoDescricao;
    private Integer quantidade;
    private double precoUnitario;
    private double subtotal;
}
