package br.com.ldf.medium.warehouse.event.consumer;

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
public class StockChangeConsumer {

    SqsAsyncClient sqsAsyncClient;
    ObjectMapper objectMapper;

    @SqsListener(
            value = "${stock.change.event.queue}",
            factory = "assessmentSqsListenerContainerFactory"
    )
    public void listenStockChange(String payload) throws JsonProcessingException {
        log.info("stage=init-listen-stock-change, payload={}", payload);
        var obj = objectMapper.readValue(payload, StockChangeEvent.class);
        log.info("stage=finish-listen-stock-change, stockChangeEvent={}", obj);
    }
}
