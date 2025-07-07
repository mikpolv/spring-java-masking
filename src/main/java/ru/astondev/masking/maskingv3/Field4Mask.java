package ru.astondev.masking.maskingv3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.astondev.masking.common.MaskingType;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Field4Mask {
    _1("sensitiveDate", MaskingType.DATE),
    _2("sensitiveInfo", MaskingType.TEXT),
    ;

    private final String fieldName;

    private final MaskingType method;

    private static final Map<String, MaskingType> FIELD_MASKING_METHODS = Arrays.stream(Field4Mask.values())
        .collect(Collectors.toMap(Field4Mask::getFieldName, Field4Mask::getMethod));

    public static MaskingType getMaskingMethod(String fieldName) {
        return FIELD_MASKING_METHODS.get(fieldName);
    }
}
