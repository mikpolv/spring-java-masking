package ru.astondev.masking.maskingv3;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(
    name = "MessageMaskPatternConverter",
    category = "Converter"
)
@ConverterKeys({"hm", "hmsg", "hmessage"})
public final class MessageMaskPatternConverter extends LogEventPatternConverter {

    private static final String REGEXP_TMP = "\"(%s)\"\\s?:\\s?\"([\\w|\\s|а-яА-я/|\\+|(|)|\\'\\./\\|\\.|\\:-]+)\"";

    private static final Map<String, Pattern> PATTERNS =
        Arrays.stream(Field4Mask.values())
            .collect(Collectors.toMap(
                Field4Mask::getFieldName,
                field -> Pattern.compile(
                    String.format(REGEXP_TMP, field.getFieldName()),
                    CASE_INSENSITIVE
                )
            ));

    private final String[] formats;
    private final Configuration config;

    private MessageMaskPatternConverter(Configuration config, String[] options) {
        super("HMessage", "hmessage");
        this.formats = options;
        this.config = config;
    }

    public static MessageMaskPatternConverter newInstance(Configuration config, String[] options) {
        return new MessageMaskPatternConverter(config, options);
    }

    public void format(LogEvent event, StringBuilder output) {
        String message = event.getMessage().getFormattedMessage();
        String maskedMessage = message;
        try {
            maskedMessage = masking(message);
        } catch (Exception e) {
            System.out.println("Failed While Masking");
            maskedMessage = message;
        }
        output.append(maskedMessage);

    }

    private String masking(final String message) {
        AtomicReference<String> atomicMsg = new AtomicReference<>(message);
        return PATTERNS.entrySet().stream()
            .map(pattern -> {
                final StringBuffer buffer = new StringBuffer();
                Matcher matcher = pattern.getValue().matcher(atomicMsg.get());
                maskMatcher(pattern.getKey(), matcher, buffer);
                atomicMsg.set(buffer.toString());
                return buffer.toString();
            })
            //.skip(PATTERNS.size() - 1)
            .findFirst()
            .orElse("");

    }

    private StringBuffer maskMatcher(String method, Matcher matcher, StringBuffer buffer) {
        while (matcher.find()) {
            String replacement = matcher.group(0)
                .replace(matcher.group(2), buildMask(method, matcher.group(2)));
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer;
    }

    private String buildMask(String method, String msg) {
//        switch (method) {
//            case "AB_ML_03":
//                return AB_ML_03(msg);
//            case "AB_ML_04":
//                return AB_ML_04(msg);
//            case "AB_ML_05":
//                return AB_ML_05(msg);
//            case "AB_ML_06":
//                return AB_ML_06(msg);
//            case "AB_ML_07":
//                return AB_ML_07(msg);
//            case "AB_ML_07_ADDRESS":
//                return AB_ML_07_ADDRESS(msg);
//            case "AB_ML_09":
//                return AB_ML_09(msg);
//            default:
//                return AB_ML_01(msg);
//        }
        return "***************";
    }
}

