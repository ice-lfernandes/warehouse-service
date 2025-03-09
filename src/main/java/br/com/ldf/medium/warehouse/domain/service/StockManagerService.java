package br.com.ldf.medium.warehouse.domain.service;

import br.com.ldf.medium.warehouse.domain.exception.DomainRuleException;
import br.com.ldf.medium.warehouse.persistence.repository.StockRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockManagerService {

    StockRepository stockRepository;

    public void addStock(Long productId, Integer quantity) {
        stockRepository.findByProductId(productId)
                .ifPresentOrElse(
                        stock -> {
                            stock.addQuantity(quantity);
                            var stockPersisted = stockRepository.save(stock);
                            log.info("stage=add-stock-success, stockPersisted={}", stockPersisted);
                        },
                        () -> {
                            throw new DomainRuleException("Product not found");
                        }
                );
    }

    public void subtractionStock(Long productId, Integer quantity) {
        stockRepository.findByProductId(productId)
                .ifPresentOrElse(
                        stock -> {
                            stock.subtractionQuantity(quantity);
                            var stockPersisted = stockRepository.save(stock);
                            log.info("stage=subtraction-stock-success, stockPersisted={}", stockPersisted);
                        },
                        () -> {
                            throw new DomainRuleException("Product not found");
                        }
                );
    }
}
