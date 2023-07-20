package com.shop.core.domain.code;

import com.shop.core.common.CodeEnum;
import com.shop.core.common.CustomEnumJpaConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CardCode implements CodeEnum {
    SHINHAN_CARD("01", "신한카드"),
    KAKAO_CARD("02", "카카오카드"),
    K_CARD("03", "K뱅크 카드");

    private final String code;
    private final String name;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @javax.persistence.Converter(autoApply = true)
    static class Converter extends CustomEnumJpaConverter<CardCode> {
        public Converter() {
            super(CardCode.class);
        }
    }
}
