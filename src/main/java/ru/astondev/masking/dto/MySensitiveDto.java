package ru.astondev.masking.dto;

import ru.astondev.masking.common.MaskingType;
import ru.astondev.masking.maskingv2.annotation.Sensitive;

import java.time.LocalDate;

public record MySensitiveDto(
    Long id,
    @Sensitive(MaskingType.ACCOUNT_NUMBER)
    String accountNumber,
    @Sensitive(MaskingType.DATE)
    LocalDate signDate
) {
}
