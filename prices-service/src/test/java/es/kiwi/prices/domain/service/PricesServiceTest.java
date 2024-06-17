package es.kiwi.prices.domain.service;

import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.application.ports.output.PricesOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
class PricesServiceTest {
    @Mock
    private PricesOutputPort pricesOutputPort;

    private PricesService pricesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pricesService = new PricesService(pricesOutputPort);
    }

    @Test
    void testGetPricesByDate_NoPricesFound() {
        when(pricesOutputPort.findPrices(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        PricesNotFoundException exception = assertThrows(PricesNotFoundException.class, () ->
                pricesService.getPricesByDate(35455L, 1L, LocalDateTime.of(2024, 6, 14, 10, 0)));

        assertEquals("Prices not found with application date:2024-06-14T10:00", exception.getMessage());
    }

    @Test
    void testGetPricesByDate_SinglePriceFound() {
        Prices prices = new Prices();
        prices.setProductId(35455L);
        prices.setBrandId(1L);
        prices.setPriority(1);

        when(pricesOutputPort.findPrices(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(prices));

        Prices result = pricesService.getPricesByDate(35455L, 1L, LocalDateTime.of(2024, 6, 14, 10, 0));

        assertEquals(prices, result);
    }

    @Test
    void testGetPricesByDate_MultiplePricesFound() {
        Prices highPriorityPrices = new Prices();
        highPriorityPrices.setProductId(35455L);
        highPriorityPrices.setBrandId(1L);
        highPriorityPrices.setPriority(2);

        Prices lowPriorityPrices = new Prices();
        lowPriorityPrices.setProductId(35455L);
        lowPriorityPrices.setBrandId(1L);
        lowPriorityPrices.setPriority(1);

        List<Prices> pricesList = Arrays.asList(lowPriorityPrices, highPriorityPrices);

        when(pricesOutputPort.findPrices(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(pricesList);

        Prices result = pricesService.getPricesByDate(35455L, 1L, LocalDateTime.of(2024, 6, 14, 10, 0));

        assertEquals(highPriorityPrices, result);
    }

}