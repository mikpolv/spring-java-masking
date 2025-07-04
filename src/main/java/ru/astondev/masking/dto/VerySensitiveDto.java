package ru.astondev.masking.dto;

import ru.astondev.masking.annotation.Sensitive;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.astondev.masking.annotation.MaskingType.DATE;

public record VerySensitiveDto(
    Long id,
    String accountNumber,
    @Sensitive(DATE)
    LocalDate sensitiveDate,
    @Sensitive
    String sensitiveInfo,
    BigDecimal amount
) {

//    @Override
//    public String toString() {
//        return "";
//    }
}
