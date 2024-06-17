package es.kiwi.prices.infrastructure.input.rest;

import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.domain.service.PricesService;
import es.kiwi.prices.infrastructure.input.rest.data.requests.PricesQueryRequest;
import es.kiwi.prices.infrastructure.input.rest.data.responses.PricesQueryResponse;
import es.kiwi.prices.infrastructure.input.rest.mapper.PricesRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PricesRestAdapterTest {

    @Autowired
    private PricesRestMapper pricesRestMapper;

    @Autowired
    private PricesRestAdapter pricesRestAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    private LocalDateTime parseApplicationDate(String applicationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(applicationDate, formatter);
    }

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("2020-06-14 10:00:00", LocalDateTime.of(2020, 6, 14, 0, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59), new BigDecimal("35.50"), 1L),
                Arguments.of("2020-06-14 16:00:00", LocalDateTime.of(2020, 6, 14, 15, 0, 0), LocalDateTime.of(2020, 6, 14, 18, 30, 0), new BigDecimal("25.45"), 2L),
                Arguments.of("2020-06-14 21:00:00", LocalDateTime.of(2020, 6, 14, 0, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59), new BigDecimal("35.50"), 1L),
                Arguments.of("2020-06-15 10:00:00", LocalDateTime.of(2020, 6, 15, 0, 0, 0), LocalDateTime.of(2020, 6, 15, 11, 0, 0), new BigDecimal("30.50"), 3L),
                Arguments.of("2020-06-16 21:00:00", LocalDateTime.of(2020, 6, 15, 16, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59), new BigDecimal("38.95"), 4L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testGetPrices_Success(String applicationDate, LocalDateTime expectedStartDate, LocalDateTime expectedEndDate, BigDecimal expectedPrice, Long expectedPriceList) {
        PricesQueryRequest pricesQueryRequest = new PricesQueryRequest();
        pricesQueryRequest.setApplicationDate(parseApplicationDate(applicationDate));
        pricesQueryRequest.setBrandId(1L);
        pricesQueryRequest.setProductId(35455L);

        Prices mockPrices = new Prices();
        mockPrices.setProductId(35455L);
        mockPrices.setBrandId(1L);
        mockPrices.setStartDate(expectedStartDate);
        mockPrices.setEndDate(expectedEndDate);
        mockPrices.setPrice(expectedPrice);
        mockPrices.setPriceList(expectedPriceList);

        PricesQueryResponse pricesQueryResponse = new PricesQueryResponse();
        pricesQueryResponse.setBrandId(1L);
        pricesQueryResponse.setProductId(35455L);
        pricesQueryResponse.setPrice(expectedPrice);
        pricesQueryResponse.setPriceList(expectedPriceList);
        pricesQueryResponse.setStartDate(expectedStartDate);
        pricesQueryResponse.setEndDate(expectedEndDate);

        ResponseEntity<PricesQueryResponse> responseEntity = pricesRestAdapter.getPrices(pricesQueryRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pricesQueryResponse, responseEntity.getBody());
    }

}