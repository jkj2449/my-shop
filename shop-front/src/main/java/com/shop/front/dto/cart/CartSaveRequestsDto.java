package com.shop.front.dto.cart;

import com.shop.core.domain.cart.Cart;
import com.shop.core.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartSaveRequestsDto {
    private Long memberId;
    private Long itemId;

    public Cart toEntity(final Item item) {
        return Cart.builder()
                .memberId(memberId)
                .item(item)
                .build();
    }
}
