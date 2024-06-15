package es.kiwi.prices.infrastructure.input.rest.data.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Prices' details retrieved")
public class PricesQueryResponse {

    private Long productId;
    private Long brandId;
    private Long priceList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @Schema(description = "final price (pvp)")
    private BigDecimal price;

}