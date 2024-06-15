package es.kiwi.prices.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Prices {
    private Long priceList;

    private Long brandId;

    private Long productId;

    private Integer priority;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal price;

    private String curr;

    private LocalDateTime applicationDate;
}
