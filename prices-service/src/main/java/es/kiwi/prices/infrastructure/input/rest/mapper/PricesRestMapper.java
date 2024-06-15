package es.kiwi.prices.infrastructure.input.rest.mapper;

import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.infrastructure.input.rest.data.requests.PricesQueryRequest;
import es.kiwi.prices.infrastructure.input.rest.data.responses.PricesQueryResponse;
import org.mapstruct.Mapper;

@Mapper
public interface PricesRestMapper {

    Prices toPrices(PricesQueryRequest pricesQueryRequest);
    PricesQueryResponse toPricesQueryResponse(Prices prices);
}