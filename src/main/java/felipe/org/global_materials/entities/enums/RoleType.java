package felipe.org.global_materials.entities.enums;

/**
 * Papeis de acesso disponiveis no sistema.
 * ADMIN: acesso total (usuarios, produtos, fornecedores, clientes, relatorios, estoque, vendas).
 * FUNCIONARIO: acesso operacional (vendas, consulta de estoque, consulta de clientes, atualizacao de estoque).
 */
public enum RoleType {
    ADMIN,
    FUNCIONARIO
}
