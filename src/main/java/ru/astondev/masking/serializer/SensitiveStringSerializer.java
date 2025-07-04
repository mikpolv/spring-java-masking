package ru.astondev.masking.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.math.BigDecimal;

// не надо
public class SensitiveStringSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        if (value == null) {
            gen.writeNull();
            return;
        }

        String result = "***";

        gen.writeString(result);
    }

    // надо ли

    @Override
    public JsonSerializer<BigDecimal> createContextual(SerializerProvider prov, BeanProperty property) {
        System.out.println("asdasdasdddd");
        if (property.getName().toLowerCase().contains("amount")) {
            return new SensitiveStringSerializer();
        }
        return this;
    }
}

