package es.kiwi.prices.domain.ports.output;

import es.kiwi.prices.domain.model.Prices;

import java.time.LocalDateTime;
import java.util.List;

public interface PricesOutputPort {

    List<Prices> findPrices(Long productId, Long brandId, LocalDateTime applicationDate);
}
