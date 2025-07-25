package ru.astondev.masking.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MaskingType {
    TEXT,
    DATE,
    ACCOUNT_NUMBER,
    BIG_DECIMAL,
}
