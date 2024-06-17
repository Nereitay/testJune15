package es.kiwi.prices.application.service;

import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.domain.ports.input.GetPricesUseCase;
import es.kiwi.prices.domain.ports.output.PricesOutputPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class PricesService implements GetPricesUseCase {

    private final PricesOutputPort pricesOutputPort;

    @Override
    public Prices getPricesByDate(Long productId, Long brandId, LocalDateTime applicationDate) {
        List<Prices> prices = pricesOutputPort.findPrices(productId, brandId, applicationDate);
        return prices.stream().max(Comparator.comparing(Prices::getPriority))
                .orElseThrow(() -> new PricesNotFoundException("Prices not found with application date:" + applicationDate));

    }
}
