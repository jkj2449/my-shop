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
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Setter
@Getter
@NoArgsConstructor
public class OrderSaveRequestDto {
    @NotNull
    private Long memberId;
    @NotNull
    private String address;
    private String cardNumber;
    private CardCode cardCode;
    private BankCode bankCode;
    @NotNull
    private PayTypeCode payTypeCode;
    @NotNull
    private Long totalPrice;
    private List<Long> cartIdList;
    @Size(min = 1)
    private List<Long> itemIdList;

    public Order toEntity(final List<Item> items) {
        return Order.builder()
                .memberId(this.memberId)
                .address(this.address)
                .cardNumber(this.cardNumber)
                .cardCode(PayTypeCode.CARD == this.payTypeCode ? this.cardCode : null)
                .bankCode(PayTypeCode.REMITTANCE == this.payTypeCode ? this.bankCode : null)
                .price(this.totalPrice)
                .orderStatusCode(OrderStatusCode.PROCESSING_PAYMENT)
                .payTypeCode(this.payTypeCode)
                .orderDetail(this.toOrderDetails(items))
                .build();
    }

    public void validatePayType() {
        if (PayTypeCode.CARD == this.payTypeCode) {
            if (nonNull(this.cardCode) && StringUtils.hasText(cardNumber)) {
                return;
            }
        }

        if (PayTypeCode.REMITTANCE == this.payTypeCode && isNull(this.bankCode)) {
            return;
        }

        throw new IllegalArgumentException("주문타입이 올바르지 않습니다.");
    }

    public void validatePrice(final List<Item> items) {
        Long itemPriceTotal = items.stream().mapToLong(Item::getPrice).sum();
        if (itemPriceTotal.equals(this.getTotalPrice())) {
            return;
        }

        throw new IllegalArgumentException("주문금액이 올바르지 않습니다.");
    }

    private List<OrderDetail> toOrderDetails(final List<Item> items) {
        return items.stream()
                .map(item -> OrderDetail.builder()
                        .price(item.getPrice())
                        .item(item)
                        .build()).collect(Collectors.toList());
    }

    @Builder
    public OrderSaveRequestDto(Long memberId, String address, String cardNumber, CardCode cardCode, BankCode bankCode, PayTypeCode payTypeCode, Long totalPrice, List<Long> cartIdList, List<Long> itemIdList) {
        this.memberId = memberId;
        this.address = address;
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.bankCode = bankCode;
        this.payTypeCode = payTypeCode;
        this.totalPrice = totalPrice;
        this.cartIdList = cartIdList;
        this.itemIdList = itemIdList;
    }
}
