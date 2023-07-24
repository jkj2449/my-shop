package com.shop.core.domain.order;

import com.shop.core.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long price;
    @OneToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @Builder
    public OrderDetail(Long price, Item item) {
        this.price = price;
        this.item = item;
    }
}
