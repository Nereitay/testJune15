package es.kiwi.prices.infrastructure.input.rest.mapper;

import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.infrastructure.input.rest.data.responses.PricesQueryResponse;
import org.mapstruct.Mapper;

@Mapper
public interface PricesRestMapper {

    PricesQueryResponse toPricesQueryResponse(Prices prices);
}