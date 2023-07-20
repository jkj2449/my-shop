package com.shop.core.domain.code;

import com.shop.core.common.CodeEnum;
import com.shop.core.common.CustomEnumJpaConverter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum OrderStatusCode implements CodeEnum {
    COMPLETED("01", "주문 완료"), FAILED("02", "주문 실패"), PENDING_PAYMENT("03", "결제 대기"), PROCESSING_PAYMENT("04", "결제 처리중");

    private final String code;
    private final String title;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return title;
    }

    public static OrderStatusCode of(String code) {
        return Arrays.stream(OrderStatusCode.values())
                .findFirst()
                .filter(v -> v.getCode().equals(code))
                .orElseThrow(() -> new IllegalArgumentException("not found match code = " + code));
    }

    @javax.persistence.Converter(autoApply = true)
    static class Converter extends CustomEnumJpaConverter<OrderStatusCode> {
        public Converter() {
            super(OrderStatusCode.class);
        }
    }
}
