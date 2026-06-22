package felipe.org.global_materials.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String telefone;
    private String email;
    private String endereco;

    @Builder.Default
    private Boolean ativo = true;

    @OneToMany(
            mappedBy = "fornecedor",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Produto> produtos = new ArrayList<>();

    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();

        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}