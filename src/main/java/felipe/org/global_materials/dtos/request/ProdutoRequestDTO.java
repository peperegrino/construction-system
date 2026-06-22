package felipe.org.global_materials.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {

    @NotBlank(message = "O código (código de barras/SKU) é obrigatório")
    @Size(max = 50, message = "O código deve ter no máximo 50 caracteres")
    private String codigo;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
    private String descricao;

    @NotNull(message = "O preço de custo é obrigatório")
    @PositiveOrZero(message = "O preço de custo não pode ser negativo")
    private double precoCusto;

    @NotNull(message = "O preço de venda é obrigatório")
    @PositiveOrZero(message = "O preço de venda não pode ser negativo")
    private double precoVenda;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa")
    private Integer quantidadeEstoque;

    @NotNull(message = "O estoque mínimo é obrigatório")
    @PositiveOrZero(message = "O estoque mínimo não pode ser negativo")
    private Integer estoqueMinimo;

    @Size(max = 500, message = "A URL da imagem deve ter no máximo 500 caracteres")
    private String imagemUrl;

    @NotNull(message = "O ID do fornecedor é obrigatório")
    private Long fornecedorId;
}