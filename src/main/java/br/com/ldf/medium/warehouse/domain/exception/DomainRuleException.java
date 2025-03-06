package br.com.ldf.medium.warehouse.domain.exception;

public class DomainRuleException extends RuntimeException {

    public DomainRuleException(String message) {
        super(message);
    }
}
