package com.shop.core.domain.code;

import com.shop.core.common.CodeEnum;
import com.shop.core.common.CustomEnumJpaConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BankCode implements CodeEnum {
    SHINHAN_BANK("01", "신한은행"),
    KAKAO_BANK("02", "카카오뱅크"),
    K_BANK("03", "K뱅크");

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
    static class Converter extends CustomEnumJpaConverter<BankCode> {
        public Converter() {
            super(BankCode.class);
        }
    }
}
