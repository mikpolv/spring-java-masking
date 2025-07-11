package ru.astondev.masking.maskingv2;

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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Slf4j
@Component
public class MaskingObjectMapper {
    public static final ObjectMapper SENSITIVE_OBJECT_MAPPER = initSencitiveObjectMapper();

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
            .registerModule(new MaskingModule()); // Our masking


    }
}
