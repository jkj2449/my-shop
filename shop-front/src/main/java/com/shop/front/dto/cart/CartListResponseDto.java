package com.shop.front.dto.cart;

import com.shop.core.domain.cart.Cart;
import com.shop.front.dto.item.ItemResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class CartListResponseDto {
    private Long id;
    private Long memberId;
    private ItemResponseDto item;
    
    public CartListResponseDto(Cart cart) {
        this.id = cart.getId();
        this.memberId = cart.getMemberId();
        this.item = new ItemResponseDto(cart.getItem());
    }
}
