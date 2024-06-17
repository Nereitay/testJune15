package es.kiwi.prices.infrastructure.output.persistence.repository;

import es.kiwi.prices.infrastructure.output.persistence.entity.PricesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PricesRepository extends JpaRepository<PricesEntity, Long> {

    @Query("select p from PricesEntity p where p.productId = :productId and p.brandId = :brandId and :applicationDate" +
            " between p.startDate and p.endDate")
    List<PricesEntity> findByProductIdAndBrandIdAndApplicationDate(@Param("productId") Long productId,@Param("brandId") Long brandId,
                                                                   @Param("applicationDate") LocalDateTime applicationDate);
}
