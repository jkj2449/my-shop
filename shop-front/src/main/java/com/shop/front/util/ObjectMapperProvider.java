package com.shop.front.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shop.front.config.JacksonConfig;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class ObjectMapperProvider {
    public static final ObjectMapper MAPPER = objectMapper();

    public static ObjectMapper objectMapper() {
        ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
                .serializerByType(Enum.class, new JacksonConfig.EnumMapperSerializer())
                .deserializerByType(Enum.class, new JacksonConfig.EnumMapperDeserializer())
                .build();

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    public static String serialization(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T deserialization(String jsonText, Class<T> valueType) {
        try {
            return MAPPER.readValue(jsonText, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T typeConvert(Object object, Class<T> convertType) {
        return MAPPER.convertValue(object, convertType);
    }
}
