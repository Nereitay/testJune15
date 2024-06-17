package es.kiwi.prices.domain.service;

import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.application.ports.input.GetPricesUseCase;
import es.kiwi.prices.application.ports.output.PricesOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PricesService implements GetPricesUseCase {

    private final PricesOutputPort pricesOutputPort;

    @Override
    public Prices getPricesByDate(Long productId, Long brandId, LocalDateTime applicationDate) {
        List<Prices> prices = pricesOutputPort.findPrices(productId, brandId, applicationDate);
        if (CollectionUtils.isEmpty(prices)) throw new PricesNotFoundException("Prices not found with application " +
                "date:" + applicationDate);

        Optional<Prices> pricesOptional = prices.stream().max(Comparator.comparing(Prices::getPriority));
        if (pricesOptional.isEmpty())
            throw new PricesNotFoundException("Prices not found with application date:" + applicationDate);
        return pricesOptional.get();
    }
}
