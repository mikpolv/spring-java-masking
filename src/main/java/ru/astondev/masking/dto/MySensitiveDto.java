package ru.astondev.masking.dto;

import java.time.LocalDate;

public record MySensitiveDto(
        Long id,
        String accountNumber,
        LocalDate signDate
) {
}
