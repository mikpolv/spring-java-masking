package ru.astondev.masking.serializer;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;

public class MaskingModule extends SimpleModule {
    @Override
    public void setupModule(Module.SetupContext context) {
        super.setupModule(context);
        context.addBeanSerializerModifier(new SensitiveSerializerModifier());

        // add contextual
        // addSerializer(BigDecimal.class, new SensitiveStringSerializer());
    }
}
