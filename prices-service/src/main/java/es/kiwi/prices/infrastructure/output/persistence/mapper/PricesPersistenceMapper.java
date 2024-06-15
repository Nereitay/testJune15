package es.kiwi.prices.infrastructure.output.persistence.mapper;

import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.infrastructure.output.persistence.entity.PricesEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PricesPersistenceMapper {

    PricesEntity toPricesEntity(Prices prices);

    Prices toPrices(PricesEntity pricesEntity);
}