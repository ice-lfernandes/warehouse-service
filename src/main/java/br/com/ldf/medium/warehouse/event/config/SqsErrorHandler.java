package br.com.ldf.medium.warehouse.event.config;

import io.awspring.cloud.sqs.listener.AsyncAdapterBlockingExecutionFailedException;
import io.awspring.cloud.sqs.listener.errorhandler.AsyncErrorHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.messaging.Message;

import java.util.concurrent.CompletableFuture;

/**
 * Error handling class for SQS messages that defines which exceptions are retryable or not.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SqsErrorHandler<T> implements AsyncErrorHandler<T> {

    Logger log;

    @Override
    public CompletableFuture<Void> handle(Message<T> message, Throwable throwable) {
        log.error("stage=error-handler, msg=error-consuming-message, message={}", message.getPayload(), throwable);

        if (throwable.getCause() instanceof AsyncAdapterBlockingExecutionFailedException) {
            // Non-retryable exception
            return CompletableFuture.completedFuture(null);
        } else {
            // For all other exceptions
            return AsyncErrorHandler.super.handle(message, throwable);
        }
    }
}