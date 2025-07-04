package ru.astondev.masking.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * When getting masking type by Class needed
 */
@Getter
@RequiredArgsConstructor
public enum MaskingTypeByClass {
    TEXT(List.of(String.class)),
    DATE(List.of(LocalDate.class, LocalDateTime.class, LocalTime.class, Instant.class, OffsetDateTime.class)),
    ACCOUNT_NUMBER(Collections.emptyList());

    private final List<Class<?>> suitableClass;

    private static final Map<Class<?>, MaskingTypeByClass> CLASS_TO_MASKING_TYPE =
        Arrays.stream(MaskingTypeByClass.values())
            .flatMap(type -> type.getSuitableClass().stream()
                .map(cls -> Map.entry(cls, type))
            )
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue)

            );

    public static <T> MaskingTypeByClass getMaskingByType(@NonNull Class<T> clazz) {
        return Optional.ofNullable(CLASS_TO_MASKING_TYPE.get(clazz))
            .orElse(MaskingTypeByClass.TEXT);
    }
}
