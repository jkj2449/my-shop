package com.shop.core.domain.cart;

import com.shop.core.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @OneToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @Builder
    public Cart(Long id, Long memberId, Item item) {
        this.id = id;
        this.memberId = memberId;
        this.item = item;
    }
}
