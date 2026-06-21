package felipe.org.global_materials.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Linha do relatorio de giro de estoque / produtos mais vendidos. */
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
