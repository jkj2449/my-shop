package com.shop.core.domain.code;

import com.shop.core.common.CodeEnum;
import com.shop.core.common.CustomEnumJpaConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PayTypeCode implements CodeEnum {
    CARD("01", "카드"), REMITTANCE("02", "무통장 입금");

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

    @javax.persistence.Converter(autoApply = true)
    static class Converter extends CustomEnumJpaConverter<PayTypeCode> {
        public Converter() {
            super(PayTypeCode.class);
        }
    }
}
