package com.shop.front.dto.order;

import com.shop.core.domain.item.Item;
import com.shop.core.domain.order.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderDetailListResponseDto {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private String imagePath;

    public static OrderDetailListResponseDto of(OrderDetail orderDetail) {
        Item item = orderDetail.getItem();

        return OrderDetailListResponseDto.builder()
                .id(orderDetail.getId())
                .price(orderDetail.getPrice())
                .name(item.getName())
                .description(item.getDescription())
                .imagePath(item.getImagePath())
                .build();
    }

    @Builder
    public OrderDetailListResponseDto(Long id, String name, Long price, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
    }
}
