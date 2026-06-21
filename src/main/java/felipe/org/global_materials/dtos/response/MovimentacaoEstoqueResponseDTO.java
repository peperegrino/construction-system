package felipe.org.global_materials.dtos.response;


import felipe.org.global_materials.entities.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoEstoqueResponseDTO {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String produtoDescricao;
    private TipoMovimentacao tipoMovimentacao;
    private Integer quantidade;
    private String observacao;
    private LocalDateTime dataMovimentacao;
}
