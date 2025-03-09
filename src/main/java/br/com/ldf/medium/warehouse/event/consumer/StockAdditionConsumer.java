package br.com.ldf.medium.warehouse.event.consumer;

import br.com.ldf.medium.warehouse.domain.service.StockManagerService;
import br.com.ldf.medium.warehouse.event.schema.StockChangeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StockAdditionConsumer {

    StockManagerService stockManagerService;

    @SqsListener(
        value = "${aws.sqs.queue.stock-addition-queue}"
    )
    public void listenAdditionStock(StockChangeEvent stockChangeEvent) {
        log.info("stage=init-listen-addition-stock, stockChangeEvent={}", stockChangeEvent);
        stockManagerService.addStock(stockChangeEvent.productId(), stockChangeEvent.quantity());
        log.info("stage=finish-listen-addition-stock");
    }
}
