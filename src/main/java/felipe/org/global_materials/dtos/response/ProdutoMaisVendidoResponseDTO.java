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
public class ProdutoMaisVendidoResponseDTO {
    private Long produtoId;
    private String codigo;
    private String descricao;
    private Long quantidadeVendida;
    private double valorTotalVendido;
}
