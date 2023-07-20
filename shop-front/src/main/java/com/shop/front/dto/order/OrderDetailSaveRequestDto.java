package com.shop.front.dto.order;

import com.shop.core.domain.item.Item;
import com.shop.core.domain.order.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailSaveRequestDto {
    private Long cartId;
    private Long itemId;
    private Long price;

    public OrderDetail toEntity() {
        return OrderDetail.builder()
                .price(this.price)
                .item(Item.builder().id(this.itemId).build())
                .build();
    }

    @Builder
    public OrderDetailSaveRequestDto(Long cartId, Long itemId, Long price) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.price = price;
    }
}
