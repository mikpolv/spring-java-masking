package ru.astondev.masking.dto;

import ru.astondev.masking.maskingv2.annotation.Sensitive;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.astondev.masking.common.MaskingType.DATE;

public record VerySensitiveDto(
    Long id,
    String accountNumber,
    @Sensitive(DATE)
    LocalDate sensitiveDate,
    @Sensitive
    String sensitiveInfo
) {

//    @Override
//    public String toString() {
//        return "";
//    }
}
