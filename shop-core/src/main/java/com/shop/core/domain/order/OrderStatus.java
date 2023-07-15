package com.shop.core.domain.order;

import com.shop.core.common.CustomEnumJpaConverter;
import com.shop.core.common.EnumMapper;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum OrderStatus implements EnumMapper {
    COMPLETED("01", "주문완료"), FAILED("02", "주문실패"), PENDING_PAYMENT("03", "결제대기");

    private final String code;
    private final String title;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static OrderStatus of(String code) {
        return Arrays.stream(OrderStatus.values())
                .findFirst()
                .filter(v -> v.getCode().equals(code))
                .orElseThrow(() -> new IllegalArgumentException("not found match code = " + code));
    }

    @javax.persistence.Converter(autoApply = true)
    static class Converter extends CustomEnumJpaConverter<OrderStatus> {
        public Converter() {
            super(OrderStatus.class);
        }
    }
}
