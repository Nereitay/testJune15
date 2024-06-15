package es.kiwi.prices.infrastructure.output.persistence.repository;

import es.kiwi.prices.infrastructure.output.persistence.entity.PricesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricesRepository extends JpaRepository<PricesEntity, Long> {
    List<PricesEntity> findByProductIdAndBrandId(Long productId, Long brandId);
}
