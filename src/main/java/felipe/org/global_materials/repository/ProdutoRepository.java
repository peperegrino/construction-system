package felipe.org.global_materials.repository;


import felipe.org.global_materials.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Produto> findByDescricaoContainingIgnoreCase(String descricao);


    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque <= p.estoqueMinimo AND p.ativo = true")
    List<Produto> findProdutosComEstoqueBaixo();
}
