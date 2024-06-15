package es.kiwi.prices.infrastructure.output.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prices")
public class PricesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long priceList;

    private Long brandId;

    private Long productId;

    private Integer priority;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal price;

    private String curr;

    private LocalDateTime lastUpdate;

    private String lastUpdateBy;
}