package es.kiwi.prices.application.ports.input;

import es.kiwi.prices.domain.model.Prices;

import java.time.LocalDateTime;

public interface GetPricesUseCase {

    Prices getPricesByDate(Long productId, Long brandId, LocalDateTime applicationDate);
}
