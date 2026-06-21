package felipe.org.global_materials.entities;

import felipe.org.global_materials.entities.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    private Integer quantidade;
    private String observacao;
    private LocalDateTime dataMovimentacao;

    @PrePersist
    public void prePersist() {
        this.dataMovimentacao = LocalDateTime.now();
    }
}