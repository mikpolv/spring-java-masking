package ru.astondev.masking.maskingv3;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import ru.astondev.masking.common.FieldMasking;
import ru.astondev.masking.common.MaskingType;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Plugin(
    name = "MessageMaskPatternConverter",
    category = "Converter"
)
@ConverterKeys({"hm", "hmsg", "hmessage"})
public final class MessageMaskPatternConverter extends LogEventPatternConverter {

    private static final String REGEXP_TMP =  "\"(%s)\"\\s?:\\s?\"([\\w\\sа-яА-Я/+()'.:|-]+)\"";

    // Precompiled patterns
    private static final Map<String, Pattern> PATTERNS = Arrays.stream(Field4Mask.values())
        .collect(Collectors.toMap(
            Field4Mask::getFieldName,
            field -> Pattern.compile(
                String.format(REGEXP_TMP, Pattern.quote(field.getFieldName()))
            )
        ));

    private MessageMaskPatternConverter() {
        super("HMessage", "hmessage");
    }

    public static MessageMaskPatternConverter newInstance(Configuration config, String[] options) {
        return new MessageMaskPatternConverter();
    }

    @Override
    public void format(LogEvent event, StringBuilder output) {
        String message = event.getMessage().getFormattedMessage();
        output.append(maskMessage(message));
    }

    private String maskMessage(String message) {
        String result = message;
        for (Map.Entry<String, Pattern> entry : PATTERNS.entrySet()) {
            StringBuilder builder = new StringBuilder();
            Matcher matcher = entry.getValue().matcher(result);
            while (matcher.find()) {
                String maskedValue = buildMask(entry.getKey(), matcher.group(2));
                matcher.appendReplacement(builder, matcher.group(0)
                    .replace(matcher.group(2), maskedValue));
            }
            matcher.appendTail(builder);
            result = builder.toString();
        }
        return result;
    }

    private String buildMask(String fieldName, String value) {
        MaskingType type = Field4Mask.getMaskingMethod(fieldName);
        if (type == null) return "*****";

        return FieldMasking.mask(value, type);
    }
}

