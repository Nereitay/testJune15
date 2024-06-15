package es.kiwi.prices.infrastructure.input.rest.data.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Schema(description = "Parameters to consult prices")
public class PricesQueryRequest {

    @NotNull(message = "applicationDate is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(type = "string", example = "2020-06-14 10:00:00")
    private LocalDateTime applicationDate;

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "brandId is required")
    private Long brandId;


}