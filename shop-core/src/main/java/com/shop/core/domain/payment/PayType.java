package com.shop.core.domain.payment;

import com.shop.core.common.CustomEnumJpaConverter;
import com.shop.core.common.EnumMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PayType implements EnumMapper {
    CARD("01", "카드"), REMITTANCE("02", "무통장 입금");

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

    @javax.persistence.Converter(autoApply = true)
    static class Converter extends CustomEnumJpaConverter<PayType> {
        public Converter() {
            super(PayType.class);
        }
    }
}
