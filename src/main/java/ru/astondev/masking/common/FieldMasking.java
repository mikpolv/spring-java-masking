package ru.astondev.masking.common;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FieldMasking {
    // TODO get Masking by class??
    public static String mask(Object obj, MaskingType maskingType) {
        return switch (maskingType) {
            case TEXT -> maskString(obj.toString());
            case DATE -> maskDate(obj.toString());
            case ACCOUNT_NUMBER -> maskAccount(obj.toString());
            case BIG_DECIMAL -> maskBigDecimal((BigDecimal) obj);
            case null -> maskString(null); // maybe make a decision depending on the class?
        };
    }

    public static String maskString(String stringToMask) {
        if (stringToMask == null) {
            return "Masked(null)string";
        }
        return "Masked_" + "*".repeat(stringToMask.length()) + "_string";
    }

    public static String maskDate(String dateToMask) {
        if (dateToMask == null) {
            return "Masked(null)string";
        }
        return "Masked_" + "*".repeat(dateToMask.length()) + "_date";
    }

    public static String maskAccount(String accountToMask) {
        if (accountToMask == null) {
            return "Masked(null)string";
        }
        return "Masked_" + "*".repeat(accountToMask.length()) + "_account";
    }

    public static String maskBigDecimal(BigDecimal toMask) {
        return "Masked_$$$_money";
    }

    public static String maskLocalDate(LocalDate s, MaskingType maskingType) {
        return "Special***Date***masking";
    }
}
