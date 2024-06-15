package es.kiwi.prices.application.ports.output;

import es.kiwi.prices.domain.model.Prices;

import java.util.Optional;

public interface PricesOutputPort {

    Optional<Prices> getPricesByDateAndProductAndBrand(Prices prices);
}
