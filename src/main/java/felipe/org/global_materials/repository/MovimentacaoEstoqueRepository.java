package felipe.org.global_materials.repository;


import felipe.org.global_materials.entities.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByProdutoIdOrderByDataMovimentacaoDesc(Long produtoId);

    List<MovimentacaoEstoque> findAllByOrderByDataMovimentacaoDesc();
}
