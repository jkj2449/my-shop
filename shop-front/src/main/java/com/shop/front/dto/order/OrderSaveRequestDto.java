package com.shop.front.dto.order;

import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderDetail;
import com.shop.core.domain.order.OrderStatus;
import com.shop.core.domain.payment.PayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class OrderSaveRequestDto {
    private Long memberId;
    private String address;
    private String cardNumber;
    private String bank;
    private Long price;
    private OrderStatus orderStatus;
    private PayType payType;
    private List<OrderDetailSaveRequestDto> orderDetailList;

    public Order toEntity() {
        return Order.builder()
                .memberId(this.memberId)
                .address(this.address)
                .cardNumber(this.cardNumber)
                .bank(this.bank)
                .price(this.price)
                .orderStatus(this.orderStatus)
                .payType(this.payType)
                .orderDetail(getOrderDetails())
                .build();
    }

    private List<OrderDetail> getOrderDetails() {
        return orderDetailList.stream().map(orderDetail ->
                OrderDetail.builder()
                        .price(orderDetail.getPrice())
                        .item(orderDetail.toEntity())
                        .build()
        ).collect(Collectors.toList());
    }
}
