package felipe.org.global_materials.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoResponseDTO {
    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private double precoCusto;
    private double precoVenda;
    private double margemLucroPercentual;
    private Integer quantidadeEstoque;
    private Integer estoqueMinimo;
    private Boolean estoqueBaixo;
    private Long fornecedorId;
    private String fornecedorNome;
    private Boolean ativo;
}