package ru.astondev.masking.maskingv2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.astondev.masking.common.MaskingType;
import ru.astondev.masking.maskingv2.annotation.Sensitive;
import ru.astondev.masking.common.FieldMasking;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


// есть еще ContextualSerializer
public class SensitiveSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(
        SerializationConfig config,
        BeanDescription beanDesc,
        List<BeanPropertyWriter> beanProperties
    ) {
        for (BeanPropertyWriter writer : beanProperties) {
            Sensitive sensitive = writer.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                writer.assignSerializer(new SensitiveSerializer(sensitive.value()));
            }
        }
        return beanProperties;
    }

    public static class SensitiveSerializer extends StdSerializer<Object> {
        private final MaskingType maskingType;

        public SensitiveSerializer(MaskingType maskingType) {
            super(Object.class);
            this.maskingType = maskingType;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

            if (value == null) {
                gen.writeNull();
                return;
            }

            String result = switch (value) {
                case String s -> FieldMasking.mask(s, maskingType);
                case LocalDate s -> FieldMasking.maskDate(s.toString());
                // Add other type handlers
                default -> value.toString();
            };

            gen.writeString(result);
        }
    }
}
