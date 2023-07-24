package com.shop.core.domain.code;

import com.shop.core.common.CodeEnum;
import com.shop.core.common.CustomEnumJpaConverter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum OutboxTypeCode implements CodeEnum {
    Pay("01", "결제");

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

    public static OutboxTypeCode of(String code) {
        return Arrays.stream(OutboxTypeCode.values())
                .findFirst()
                .filter(v -> v.getCode().equals(code))
                .orElseThrow(() -> new IllegalArgumentException("not found match code = " + code));
    }

    @javax.persistence.Converter(autoApply = true)
    static class Converter extends CustomEnumJpaConverter<OutboxTypeCode> {
        public Converter() {
            super(OutboxTypeCode.class);
        }
    }
}
