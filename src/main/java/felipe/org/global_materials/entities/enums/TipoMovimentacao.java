package felipe.org.global_materials.entities.enums;

/**
 * Tipo de movimentacao de estoque.
 * ENTRADA: recebimento de mercadoria (compra de fornecedor, ajuste positivo).
 * SAIDA: baixa de estoque (venda, ajuste negativo, perda).
 */
public enum TipoMovimentacao {
    ENTRADA,
    SAIDA
}
