package es.kiwi.prices.infrastructure.output.persistence;

import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.application.ports.output.PricesOutputPort;
import es.kiwi.prices.infrastructure.output.persistence.entity.PricesEntity;
import es.kiwi.prices.infrastructure.output.persistence.mapper.PricesPersistenceMapper;
import es.kiwi.prices.infrastructure.output.persistence.repository.PricesRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PricesPersistenceAdapter implements PricesOutputPort {

    private final PricesRepository pricesRepository;

    private final PricesPersistenceMapper pricesPersistenceMapper;

    @Override
    public List<Prices> findPrices (Long productId, Long brandId, LocalDateTime applicationDate) {
        List<PricesEntity> pricesEntities = pricesRepository.findByProductIdAndBrandIdAndApplicationDate(productId,
                brandId, applicationDate);
        return pricesEntities.stream().map(pricesPersistenceMapper :: toPrices).toList();
    }
}
