package com.shop.core.common;

import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

@RequiredArgsConstructor
public abstract class CustomEnumJpaConverter<T extends Enum<T> & EnumMapper> implements AttributeConverter<T, String> {
    private final Class<T> clazz;

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute.getCode();
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + dbData));
    }
}
