package br.com.ldf.medium.warehouse.domain.service;

import br.com.ldf.medium.warehouse.domain.exception.DomainRuleException;
import br.com.ldf.medium.warehouse.persistence.repository.StockRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
                            stockRepository.save(stock);
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
                            stockRepository.save(stock);
                        },
                        () -> {
                            throw new DomainRuleException("Product not found");
                        }
                );
    }
}
