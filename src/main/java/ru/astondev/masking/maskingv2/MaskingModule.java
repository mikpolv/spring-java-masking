package ru.astondev.masking.maskingv2;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MaskingModule extends SimpleModule {
    @Override
    public void setupModule(Module.SetupContext context) {
        super.setupModule(context);
        context.addBeanSerializerModifier(new SensitiveSerializerModifier());
    }
}
