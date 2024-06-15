package es.kiwi.prices.infrastructure.output.persistence;

import es.kiwi.prices.application.ports.output.PricesOutputPort;
import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.infrastructure.output.persistence.entity.PricesEntity;
import es.kiwi.prices.infrastructure.output.persistence.mapper.PricesPersistenceMapper;
import es.kiwi.prices.infrastructure.output.persistence.repository.PricesRepository;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PricesPersistenceAdapter implements PricesOutputPort {

    private final PricesRepository pricesRepository;

    private final PricesPersistenceMapper pricesPersistenceMapper;

    @Override
    public Optional<Prices> getPricesByDateAndProductAndBrand(Prices prices) {
        List<PricesEntity> pricesEntities = pricesRepository.findByProductIdAndBrandId(prices.getProductId(), prices.getBrandId());
        if (pricesEntities.isEmpty()) return Optional.empty();

        Optional<PricesEntity> pricesEntityOptional =
                pricesEntities.stream()
                        .filter(pricesEntity -> prices.getApplicationDate().isAfter(pricesEntity.getStartDate()) && prices.getApplicationDate().isBefore(pricesEntity.getEndDate()))
                        .max(Comparator.comparing(PricesEntity::getPriority));
        return pricesEntityOptional.map(pricesPersistenceMapper::toPrices);
    }
}
