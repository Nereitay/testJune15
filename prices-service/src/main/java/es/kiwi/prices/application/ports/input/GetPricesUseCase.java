package es.kiwi.prices.application.ports.input;

import es.kiwi.prices.domain.model.Prices;

public interface GetPricesUseCase {

    Prices getPricesByDate(Prices prices);
}
