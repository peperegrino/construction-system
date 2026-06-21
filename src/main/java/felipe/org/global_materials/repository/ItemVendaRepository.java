package felipe.org.global_materials.repository;

import felipe.org.global_materials.dtos.response.ProdutoMaisVendidoResponseDTO;
import felipe.org.global_materials.entities.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    @Query("SELECT new felipe.org.global_materials.dtos.response.ProdutoMaisVendidoResponseDTO(" +
            "p.id, p.codigo, p.descricao, SUM(iv.quantidade), CAST(SUM(iv.subtotal) AS double)) " +
            "FROM ItemVenda iv JOIN iv.produto p JOIN iv.venda v " +
            "WHERE (:inicio IS NULL OR v.dataVenda >= :inicio) " +
            "AND (:fim IS NULL OR v.dataVenda <= :fim) " +
            "GROUP BY p.id, p.codigo, p.descricao " +
            "ORDER BY SUM(iv.quantidade) DESC")
    List<ProdutoMaisVendidoResponseDTO> findProdutosMaisVendidos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}