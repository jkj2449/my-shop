package com.shop.front.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.shop.core.common.CodeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class JacksonConfig {
    @Bean
    public static ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder()
                .serializerByType(Enum.class, new EnumMapperSerializer())
                .deserializerByType(Enum.class, new EnumMapperDeserializer())
                .build();
    }

    static class EnumMapperSerializer extends StdSerializer<Enum<?>> {

        public EnumMapperSerializer() {
            this(null);
        }

        @SuppressWarnings("unchecked")
        protected EnumMapperSerializer(Class<?> t) {
            super((Class<Enum<?>>) t);
        }

        @Override
        public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (CodeEnum.class.isAssignableFrom(value.getClass())) {
                gen.writeString(((CodeEnum) value).getCode());
                return;
            }

            gen.writeString(value.name());
        }
    }

    static class EnumMapperDeserializer extends StdDeserializer<Enum<?>> implements ContextualDeserializer {
        public EnumMapperDeserializer() {
            this(null);
        }

        protected EnumMapperDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();

            if (value == null || value.isEmpty()) {
                return null;
            }

            return (Enum<?>) Arrays.stream(this._valueClass.getEnumConstants())
                    .filter(e -> {
                        if (CodeEnum.class.isAssignableFrom(this._valueClass)) {
                            return ((CodeEnum) e).getCode().equals(value);
                        } else {
                            return ((Enum<?>) e).name().equals(value);
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown: " + value));

        }

        @Override
        public JsonDeserializer<Enum<?>> createContextual(DeserializationContext ctxt, BeanProperty property) {
            return new EnumMapperDeserializer(property.getType().getRawClass());
        }
    }
}
