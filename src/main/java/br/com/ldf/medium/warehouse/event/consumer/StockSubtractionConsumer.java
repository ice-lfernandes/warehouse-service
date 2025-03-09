package br.com.ldf.medium.warehouse.event.consumer;

import br.com.ldf.medium.warehouse.domain.service.StockManagerService;
import br.com.ldf.medium.warehouse.event.schema.StockChangeEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StockSubtractionConsumer {

    StockManagerService stockManagerService;

    @SqsListener(
        value = "${aws.sqs.queue.stock-subtraction-queue}"
    )
    public void listenSubtractionStock(StockChangeEvent stockChangeEvent) {
        log.info("stage=init-listen-subtraction-stock, stockChangeEvent={}", stockChangeEvent);
        stockManagerService.subtractionStock(stockChangeEvent.productId(), stockChangeEvent.quantity());
        log.info("stage=finish-listen-subtraction-stock");
    }

}