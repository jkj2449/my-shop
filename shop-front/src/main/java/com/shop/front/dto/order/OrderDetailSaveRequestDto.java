package com.shop.front.dto.order;

import com.shop.core.domain.item.Item;
import lombok.Getter;

@Getter
public class OrderDetailSaveRequestDto {
    private Long itemId;
    private Long price;

    public Item toEntity() {
        return Item.builder()
                .id(itemId)
                .build();
    }
}
