package es.kiwi.prices.domain.service;

import es.kiwi.prices.application.ports.input.GetPricesUseCase;
import es.kiwi.prices.application.ports.output.PricesOutputPort;
import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.domain.model.Prices;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PricesService implements GetPricesUseCase {

    private final PricesOutputPort pricesOutputPort;

    @Override
    public Prices getPricesByDate(Prices prices) {
        return pricesOutputPort.getPricesByDateAndProductAndBrand(prices).orElseThrow(() -> new PricesNotFoundException(
                "Prices not found with application date:" + prices.getApplicationDate()));
    }
}
