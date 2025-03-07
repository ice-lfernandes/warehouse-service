package br.com.ldf.medium.warehouse.event.consumer;

import br.com.ldf.medium.warehouse.domain.service.StockManagerService;
import br.com.ldf.medium.warehouse.event.config.SqsErrorHandler;
import br.com.ldf.medium.warehouse.event.schema.StockChangeEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StockAdditionConsumer {

    StockManagerService stockManagerService;

    @SqsListener(
        value = "${aws.sqs.queue.stock-addition-queue}",
        factory = "stockAdditionListenerContainerFactory"
    )
    public void listenAdditionStock(StockChangeEvent stockChangeEvent) {
        log.info("stage=init-listen-addition-stock, stockChangeEvent={}", stockChangeEvent);
        stockManagerService.addStock(stockChangeEvent.productId(), stockChangeEvent.quantity());
        log.info("stage=finish-listen-addition-stock");
    }

    @Bean
    public SqsMessageListenerContainerFactory<StockChangeEvent> stockAdditionListenerContainerFactory(
        SqsAsyncClient sqsAsyncClient
    ) {
        return SqsMessageListenerContainerFactory.<StockChangeEvent>builder()
            .configure(options -> {
                // Define acknowledgment policy as success, i.e., it is removed from the queue ONLY after successful processing
                options.acknowledgementMode(AcknowledgementMode.ON_SUCCESS);
            })
            .errorHandler(new SqsErrorHandler<>(log))
            .sqsAsyncClient(sqsAsyncClient)
            .build();
    }

}
