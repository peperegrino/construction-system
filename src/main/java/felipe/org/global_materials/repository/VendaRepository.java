package felipe.org.global_materials.repository;

import felipe.org.global_materials.dtos.response.FluxoCaixaDiarioResponseDTO;
import felipe.org.global_materials.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Venda> findByClienteId(Long clienteId);

    List<Venda> findAllByOrderByDataVendaDesc();

    @Query("SELECT new felipe.org.global_materials.dtos.response.FluxoCaixaDiarioResponseDTO(" +
            "CAST(v.dataVenda AS localdate), COUNT(v), CAST(SUM(v.valorTotal) AS double)) " +
            "FROM Venda v " +
            "WHERE (:inicio IS NULL OR v.dataVenda >= :inicio) " +
            "AND (:fim IS NULL OR v.dataVenda <= :fim) " +
            "GROUP BY CAST(v.dataVenda AS localdate) " +
            "ORDER BY CAST(v.dataVenda AS localdate) DESC")
    List<FluxoCaixaDiarioResponseDTO> findFluxoCaixaDiario(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}