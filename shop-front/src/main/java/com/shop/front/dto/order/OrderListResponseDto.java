package com.shop.front.dto.order;

import com.shop.core.domain.code.BankCode;
import com.shop.core.domain.code.CardCode;
import com.shop.core.domain.code.OrderStatusCode;
import com.shop.core.domain.code.PayTypeCode;
import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
    private BankCode bankCode;
    private CardCode cardCode;
    private Long price;
    private OrderStatusCode orderStatusCode;
    private PayTypeCode payTypeCode;
    private List<OrderDetailListResponseDto> orderDetailList;

    public static OrderListResponseDto of(Order order) {

        List<OrderDetail> orderDetails = order.getOrderDetail();
        List<OrderDetailListResponseDto> orderDetailListResponseDtoList = CollectionUtils.isEmpty(orderDetails) ?
                Collections.emptyList() : orderDetails.stream().map(OrderDetailListResponseDto::of).collect(Collectors.toList());

        return OrderListResponseDto.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .address(order.getAddress())
                .cardNumber(order.getCardNumber())
                .bankCode(order.getBankCode())
                .cardCode(order.getCardCode())
                .price(order.getPrice())
                .orderStatusCode(order.getOrderStatusCode())
                .payTypeCode(order.getPayTypeCode())
                .orderDetailList(orderDetailListResponseDtoList)
                .build();
    }

    @Builder
    public OrderListResponseDto(Long id, Long memberId, String address, String cardNumber, BankCode bankCode, CardCode cardCode, Long price, OrderStatusCode orderStatusCode, PayTypeCode payTypeCode, List<OrderDetailListResponseDto> orderDetailList) {
        this.id = id;
        this.memberId = memberId;
        this.address = address;
        this.cardNumber = cardNumber;
        this.bankCode = bankCode;
        this.cardCode = cardCode;
        this.price = price;
        this.orderStatusCode = orderStatusCode;
        this.payTypeCode = payTypeCode;
        this.orderDetailList = orderDetailList;
    }
}
