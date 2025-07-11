package ru.astondev.masking.maskingv1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomObjectMapper {
    public static final ObjectMapper OBJECT_MAPPER = initObjectMapper();

    private static ObjectMapper initObjectMapper() {
        return new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
    }

    public JsonNode readTree(String content) throws JsonProcessingException {
        return OBJECT_MAPPER.readTree(content);
    }

    public <T> String writeValueAsString(T value) {
        try {
            return CustomObjectMapper.OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Получена ошибка при попытке сериализации:", e);
            return "";
        }
    }

    public <T> T read(String value, TypeReference<T> typeReference) {
        try {
            return CustomObjectMapper.OBJECT_MAPPER.readValue(value, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Получена ошибка при попытке десериализации:", e);
            return null;
        }
    }
}
