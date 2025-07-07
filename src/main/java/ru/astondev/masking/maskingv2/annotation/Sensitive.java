package ru.astondev.masking.maskingv2.annotation;

import ru.astondev.masking.common.MaskingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {
    MaskingType value() default MaskingType.TEXT;
}
