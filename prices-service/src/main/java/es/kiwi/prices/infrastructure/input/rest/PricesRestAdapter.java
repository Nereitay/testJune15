package es.kiwi.prices.infrastructure.input.rest;

import es.kiwi.prices.domain.model.Prices;
import es.kiwi.prices.domain.ports.input.GetPricesUseCase;
import es.kiwi.prices.infrastructure.input.rest.data.requests.PricesQueryRequest;
import es.kiwi.prices.infrastructure.input.rest.data.responses.PricesQueryResponse;
import es.kiwi.prices.infrastructure.input.rest.mapper.PricesRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PricesRestAdapter {

    private final GetPricesUseCase getPricesUseCase;
    private final PricesRestMapper pricesRestMapper;

    @Tag(name = "/clients")
    @Operation(summary = "This method is used to get prices.")
    @GetMapping(value = "/prices")
    public ResponseEntity<PricesQueryResponse> getPrices(@Valid PricesQueryRequest request) {
        Prices prices = getPricesUseCase.getPricesByDate(request.getProductId(), request.getBrandId(), request.getApplicationDate());
        return new ResponseEntity<>(pricesRestMapper.toPricesQueryResponse(prices), HttpStatus.OK);
    }
}
