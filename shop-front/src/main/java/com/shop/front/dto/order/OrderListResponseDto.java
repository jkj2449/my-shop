package com.shop.front.dto.order;

import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderDetail;
import com.shop.core.domain.order.OrderStatus;
import com.shop.core.domain.payment.PayType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class OrderListResponseDto {
    private Long id;
    private Long memberId;
    private String address;
    private String cardNumber;
    private String bank;
    private Long price;
    private OrderStatus orderStatus;
    private PayType payType;
    private List<OrderDetailListResponseDto> orderDetailList;

    public static OrderListResponseDto of(Order order) {

        List<OrderDetail> orderDetails = order.getOrderDetail();
        List<OrderDetailListResponseDto> orderDetailListResponseDtoList = orderDetails.stream().map(OrderDetailListResponseDto::of).collect(Collectors.toList());

        return OrderListResponseDto.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .address(order.getAddress())
                .cardNumber(order.getCardNumber())
                .bank(order.getCardNumber())
                .price(order.getPrice())
                .orderStatus(order.getOrderStatus())
                .payType(order.getPayType())
                .orderDetailList(orderDetailListResponseDtoList)
                .build();
    }

    @Builder
    public OrderListResponseDto(Long id, Long memberId, String address, String cardNumber, String bank, Long price, OrderStatus orderStatus, PayType payType, List<OrderDetailListResponseDto> orderDetailList) {
        this.id = id;
        this.memberId = memberId;
        this.address = address;
        this.cardNumber = cardNumber;
        this.bank = bank;
        this.price = price;
        this.orderStatus = orderStatus;
        this.payType = payType;
        this.orderDetailList = orderDetailList;
    }
}
