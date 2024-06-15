package es.kiwi.prices.infrastructure.output.persistence;

import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.infrastructure.output.persistence.entity.PricesEntity;
import es.kiwi.prices.infrastructure.output.persistence.mapper.PricesPersistenceMapper;
import es.kiwi.prices.infrastructure.output.persistence.repository.PricesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PricesPersistenceAdapterTest {
    @Mock
    private PricesRepository pricesRepository;

    @Mock
    private PricesPersistenceMapper pricesPersistenceMapper;

    @InjectMocks
    private PricesPersistenceAdapter pricesPersistenceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPricesByDateAndProductAndBrand_PriceFound() {
        Prices prices = new Prices();
        prices.setProductId(35455L);
        prices.setBrandId(1L);
        prices.setApplicationDate(LocalDateTime.of(2024, 6, 14, 10, 0));

        PricesEntity pricesEntity1 = new PricesEntity();
        pricesEntity1.setId(1L);
        pricesEntity1.setPriceList(1L);
        pricesEntity1.setBrandId(1L);
        pricesEntity1.setProductId(35455L);
        pricesEntity1.setPriority(1);
        pricesEntity1.setStartDate(LocalDateTime.of(2024, 6, 14, 0, 0));
        pricesEntity1.setEndDate(LocalDateTime.of(2024, 12, 31, 23, 59));
        pricesEntity1.setPrice(new BigDecimal("10.99"));
        pricesEntity1.setCurr("EUR");
        pricesEntity1.setLastUpdate(LocalDateTime.of(2024, 6, 14, 9, 0));
        pricesEntity1.setLastUpdateBy("admin");

        PricesEntity pricesEntity2 = new PricesEntity();
        pricesEntity2.setId(2L);
        pricesEntity2.setPriceList(1L);
        pricesEntity2.setBrandId(1L);
        pricesEntity2.setProductId(35455L);
        pricesEntity2.setPriority(2);
        pricesEntity2.setStartDate(LocalDateTime.of(2024, 6, 14, 0, 0));
        pricesEntity2.setEndDate(LocalDateTime.of(2024, 12, 31, 23, 59));
        pricesEntity2.setPrice(new BigDecimal("15.49"));
        pricesEntity2.setCurr("EUR");
        pricesEntity2.setLastUpdate(LocalDateTime.of(2024, 6, 14, 10, 0));
        pricesEntity2.setLastUpdateBy("admin");

        List<PricesEntity> pricesEntities = Arrays.asList(pricesEntity1, pricesEntity2);

        when(pricesRepository.findByProductIdAndBrandId(35455L, 1L)).thenReturn(pricesEntities);
        when(pricesPersistenceMapper.toPrices(pricesEntity2)).thenReturn(prices);

        Optional<Prices> result = pricesPersistenceAdapter.getPricesByDateAndProductAndBrand(prices);

        assertTrue(result.isPresent());
        Prices resultPrices = result.get();
        assertEquals(prices.getProductId(), resultPrices.getProductId());
        assertEquals(prices.getBrandId(), resultPrices.getBrandId());
        assertEquals(prices.getApplicationDate(), resultPrices.getApplicationDate());
    }

    @Test
    void testGetPricesByDateAndProductAndBrand_NoPriceFound() {
        Prices prices = new Prices();
        prices.setProductId(35455L);
        prices.setBrandId(1L);
        prices.setApplicationDate(LocalDateTime.of(2024, 6, 14, 10, 0));

        List<PricesEntity> pricesEntities = Arrays.asList();

        when(pricesRepository.findByProductIdAndBrandId(35455L, 1L)).thenReturn(pricesEntities);

        Optional<Prices> result = pricesPersistenceAdapter.getPricesByDateAndProductAndBrand(prices);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetPricesByDateAndProductAndBrand_PriceNotInDateRange() {
        Prices prices = new Prices();
        prices.setProductId(35455L);
        prices.setBrandId(1L);
        prices.setApplicationDate(LocalDateTime.of(2024, 6, 14, 10, 0));

        PricesEntity pricesEntity = new PricesEntity();
        pricesEntity.setStartDate(LocalDateTime.of(2024, 6, 14, 12, 0));
        pricesEntity.setEndDate(LocalDateTime.of(2024, 12, 31, 23, 59));
        pricesEntity.setPriority(1);

        List<PricesEntity> pricesEntities = Arrays.asList(pricesEntity);

        when(pricesRepository.findByProductIdAndBrandId(35455L, 1L)).thenReturn(pricesEntities);

        Optional<Prices> result = pricesPersistenceAdapter.getPricesByDateAndProductAndBrand(prices);

        assertFalse(result.isPresent());
    }
}