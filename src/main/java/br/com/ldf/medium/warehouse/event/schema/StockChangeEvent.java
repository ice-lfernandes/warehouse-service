package br.com.ldf.medium.warehouse.event.schema;

public record StockChangeEvent(
    Long productId,
    Integer quantity
) {
}
