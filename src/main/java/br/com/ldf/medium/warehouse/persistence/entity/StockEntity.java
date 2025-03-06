package br.com.ldf.medium.warehouse.persistence.entity;

import br.com.ldf.medium.warehouse.domain.exception.DomainRuleException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "product")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_product", nullable = false, referencedColumnName = "id_product")
    ProductEntity product;
    @Column(nullable = false)
    int quantity;

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        if (quantity > this.quantity) {
            throw new DomainRuleException("Insufficient quantity");
        }
        this.quantity -= quantity;
    }
}
