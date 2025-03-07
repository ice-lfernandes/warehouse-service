package br.com.ldf.medium.warehouse.event.config;

import br.com.ldf.medium.warehouse.domain.exception.DomainRuleException;
import io.awspring.cloud.sqs.listener.errorhandler.AsyncErrorHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Error handling class for SQS messages that defines which exceptions are retryable or not.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SqsErrorHandler<T> implements AsyncErrorHandler<T> {

    Logger log;
    Set<Class<? extends Throwable>> nonRetryableExceptions = Set.of(
        MessageConversionException.class,
        DomainRuleException.class
    );

    @Override
    public CompletableFuture<Void> handle(Message<T> message, Throwable throwable) {
        log.error("stage=error-handler, msg=error-consuming-message, message={}", message.getPayload(), throwable);

        return checkNonRetryableException(throwable) ? CompletableFuture.completedFuture(null)
            : AsyncErrorHandler.super.handle(message, throwable);
    }

    /**
     * Checks if the given throwable or any of its causes is a non-retryable exception.
     *
     * @param throwable the exception to check
     * @return true if the exception or any of its causes is non-retryable, false otherwise
     */
    private boolean checkNonRetryableException(Throwable throwable) {
        while (throwable != null) {
            if (nonRetryableExceptions.contains(throwable.getClass())) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }
}