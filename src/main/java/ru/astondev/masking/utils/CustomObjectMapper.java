package ru.astondev.masking.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.astondev.masking.serializer.MaskingModule;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Slf4j
@Component
public class CustomObjectMapper {
    public static final ObjectMapper OBJECT_MAPPER = initObjectMapper();
    public static final ObjectMapper OBJECT_SENSITIVE_MAPPER = initSencitiveObjectMapper();

    private static ObjectMapper initSencitiveObjectMapper() {
        return new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .registerModule(new MaskingModule()) // Our masking
            // module
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private static ObjectMapper initObjectMapper() {
        return new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public <T> T readValue(String value, TypeReference<T> typeReference) {
        return read(value, typeReference, OBJECT_MAPPER);
    }

    public <T> String writeValueAsString(T value) {
        return write(value, OBJECT_MAPPER);
    }

    public JsonNode readTree(String content) throws JsonProcessingException {
        return OBJECT_MAPPER.readTree(content);
    }

    private <T> String write(T value, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Получена ошибка при попытке сериализации:", e);
            return "";
        }
    }

    private <T> T read(String value, Class<T> clazz, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            log.error("Получена ошибка при попытке десериализации:", e);
            return null;
        }
    }

    private <T> T read(String value, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(value, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Получена ошибка при попытке десериализации:", e);
            return null;
        }
    }

    public <T> String writeValueAsMaskedString(T value) {
        return write(value, OBJECT_SENSITIVE_MAPPER);
    }
}
