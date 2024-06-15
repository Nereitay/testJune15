package es.kiwi.prices.domain.service;

import es.kiwi.prices.application.ports.output.PricesOutputPort;
import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.domain.model.Prices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PricesServiceTest {
    @Mock
    private PricesOutputPort pricesOutputPort;

    @InjectMocks
    private PricesService pricesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPricesByDate_Success() {
        Prices mockPrices = new Prices();
        mockPrices.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0, 0));
        mockPrices.setBrandId(1L);
        mockPrices.setProductId(35455L);
        mockPrices.setPrice(new BigDecimal("100.00"));

        when(pricesOutputPort.getPricesByDateAndProductAndBrand(mockPrices)).thenReturn(Optional.of(mockPrices));

        Prices result = pricesService.getPricesByDate(mockPrices);

        assertEquals(mockPrices, result);
    }

    @Test
    void testGetPricesByDate_NotFound() {
        Prices mockPrices = new Prices();
        mockPrices.setApplicationDate(LocalDateTime.of(2024, 6, 15, 10, 0, 0));
        mockPrices.setBrandId(1L);
        mockPrices.setProductId(35455L);

        when(pricesOutputPort.getPricesByDateAndProductAndBrand(mockPrices)).thenReturn(Optional.empty());

        PricesNotFoundException exception = assertThrows(PricesNotFoundException.class, () -> pricesService.getPricesByDate(mockPrices));
        assertEquals("Prices not found with application date:2024-06-15T10:00", exception.getMessage());
    }

}