package felipe.org.global_materials.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    private String nome;
    private String descricao;
    private double precoCusto;
    private double precoVenda;

    @Builder.Default
    private Integer quantidadeEstoque = 0;

    @Builder.Default
    private Integer estoqueMinimo = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Builder.Default
    private Boolean ativo = true;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        if (this.ativo == null) {
            this.ativo = true;
        }
        if (this.quantidadeEstoque == null) {
            this.quantidadeEstoque = 0;
        }
        if (this.estoqueMinimo == null) {
            this.estoqueMinimo = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean isEstoqueBaixo() {
        return this.quantidadeEstoque != null
                && this.estoqueMinimo != null
                && this.quantidadeEstoque <= this.estoqueMinimo;
    }
}