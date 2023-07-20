package com.shop.front.common.code;

import com.shop.core.common.CodeEnum;
import com.shop.core.domain.code.BankCode;
import com.shop.core.domain.code.CardCode;
import com.shop.core.domain.code.OrderStatusCode;
import com.shop.core.domain.code.PayTypeCode;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CodeProvider {
    private Map<String, List<CodeEnumValue>> unmodifiableCodeMap;

    public Map<String, List<CodeEnumValue>> getCodes() {
        if (unmodifiableCodeMap == null || unmodifiableCodeMap.isEmpty()) {
            this.createCodes();
        }

        return unmodifiableCodeMap;
    }

    private void createCodes() {
        unmodifiableCodeMap = Map.of(
                PayTypeCode.class.getSimpleName(), toCodeEnumValues(PayTypeCode.class),
                OrderStatusCode.class.getSimpleName(), toCodeEnumValues(OrderStatusCode.class),
                BankCode.class.getSimpleName(), toCodeEnumValues(BankCode.class),
                CardCode.class.getSimpleName(), toCodeEnumValues(CardCode.class)
        );
    }

    private List<CodeEnumValue> toCodeEnumValues(Class<? extends CodeEnum> clazz) {
        return Arrays.asList(clazz.getEnumConstants()).stream()
                .map(codeEnum -> new CodeEnumValue(codeEnum))
                .collect(Collectors.toList());
    }

}
