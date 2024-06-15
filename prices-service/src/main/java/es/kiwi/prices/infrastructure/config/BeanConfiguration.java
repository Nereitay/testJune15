package es.kiwi.prices.infrastructure.config;

import es.kiwi.prices.domain.service.PricesService;
import es.kiwi.prices.infrastructure.output.persistence.PricesPersistenceAdapter;
import es.kiwi.prices.infrastructure.output.persistence.mapper.PricesPersistenceMapper;
import es.kiwi.prices.infrastructure.output.persistence.repository.PricesRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public PricesPersistenceAdapter pricesPersistenceAdapter(PricesRepository pricesRepository,
                                                             PricesPersistenceMapper pricesPersistenceMapper) {
        return new PricesPersistenceAdapter(pricesRepository, pricesPersistenceMapper);
    }

    @Bean
    public PricesService pricesService(PricesPersistenceAdapter pricesPersistenceAdapter) {
        return new PricesService(pricesPersistenceAdapter);
    }
}
