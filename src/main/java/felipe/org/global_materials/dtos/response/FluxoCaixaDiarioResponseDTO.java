package felipe.org.global_materials.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Linha do relatorio de fluxo de caixa diario. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FluxoCaixaDiarioResponseDTO {
    private LocalDate data;
    private Long quantidadeVendas;
    private double valorTotal;
}
