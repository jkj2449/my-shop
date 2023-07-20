package com.shop.front.dto.order;

import com.shop.core.domain.code.BankCode;
import com.shop.core.domain.code.CardCode;
import com.shop.core.domain.code.OrderStatusCode;
import com.shop.core.domain.code.PayTypeCode;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class OrderSaveRequestDto {
    @Min(1)
    private Long memberId;
    @NotNull
    private String address;
    private String cardNumber;
    private CardCode cardCode;
    private BankCode bankCode;
    @NotNull
    private PayTypeCode payTypeCode;
    @Size(min = 1)
    private List<OrderDetailSaveRequestDto> orderDetailList;

    public Order toEntity() {
        return Order.builder()
                .memberId(this.memberId)
                .address(this.address)
                .cardNumber(this.cardNumber)
                .cardCode(this.cardCode)
                .bankCode(this.bankCode)
                .price(this.getItemsPriceTotalSum())
                .orderStatusCode(this.getOrderStatusCode())
                .payTypeCode(this.payTypeCode)
                .orderDetail(toOrderDetails())
                .build();
    }

    public List<Long> getItemIdList() {
        return this.orderDetailList
                .stream()
                .map(item -> item.getItemId())
                .collect(Collectors.toList());
    }

    public List<Long> getCartIdList() {
        return this.orderDetailList
                .stream()
                .map(item -> item.getItemId())
                .collect(Collectors.toList());
    }

    public Long getItemsPriceTotalSum() {
        return this.orderDetailList.stream().mapToLong(item -> item.getPrice()).sum();
    }

    public boolean isValidPrice(List<Item> items) {
        if (items.size() != this.orderDetailList.size()) {
            return false;
        }

        Long itemPriceTotalSum = items.stream().mapToLong(item -> item.getPrice()).sum();
        if (this.getItemsPriceTotalSum().equals(itemPriceTotalSum)) {
            return true;
        }

        return false;
    }

    private List<OrderDetail> toOrderDetails() {
        return this.orderDetailList.stream()
                .map(OrderDetailSaveRequestDto::toEntity)
                .collect(Collectors.toList());
    }

    private OrderStatusCode getOrderStatusCode() {
        if (PayTypeCode.CARD.equals(payTypeCode.getCode())) {
            return OrderStatusCode.PROCESSING_PAYMENT;
        }

        return OrderStatusCode.PENDING_PAYMENT;
    }

    @Builder
    public OrderSaveRequestDto(Long memberId, String address, String cardNumber, CardCode cardCode, BankCode bankCode, PayTypeCode payTypeCode, List<OrderDetailSaveRequestDto> orderDetailList) {
        this.memberId = memberId;
        this.address = address;
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.bankCode = bankCode;
        this.payTypeCode = payTypeCode;
        this.orderDetailList = orderDetailList;
    }
}
