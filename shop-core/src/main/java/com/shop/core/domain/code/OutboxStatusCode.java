package com.shop.core.domain.code;

import com.shop.core.common.CodeEnum;
import com.shop.core.common.CustomEnumJpaConverter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum OutboxStatusCode implements CodeEnum {
    PENDING("01", "대기"), SUCCEED("02", "성공"), FAILED("03", "실패"), FAIL_COMPLETED("04", "실패 처리 완료");

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

    public static OutboxStatusCode of(String code) {
        return Arrays.stream(OutboxStatusCode.values())
                .findFirst()
                .filter(v -> v.getCode().equals(code))
                .orElseThrow(() -> new IllegalArgumentException("not found match code = " + code));
    }

    @javax.persistence.Converter(autoApply = true)
    static class Converter extends CustomEnumJpaConverter<OutboxStatusCode> {
        public Converter() {
            super(OutboxStatusCode.class);
        }
    }
}
