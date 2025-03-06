package br.com.ldf.medium.warehouse.persistence.repository;

import br.com.ldf.medium.warehouse.persistence.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity, Long> {

    @Query("SELECT s FROM StockEntity s JOIN FETCH s.product p WHERE p.id = :id")
    Optional<StockEntity> findByProductId(Long id);
}
