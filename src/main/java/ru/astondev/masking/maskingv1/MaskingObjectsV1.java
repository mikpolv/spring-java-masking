package ru.astondev.masking.maskingv1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ru.astondev.masking.common.FieldMasking.maskString;

@Slf4j
@RequiredArgsConstructor
public class MaskingObjectsV1 {

    private static final CustomObjectMapper customObjectMapper = new CustomObjectMapper();

    public static String getMaskingForObject(Object content) {
        return processJsonString(customObjectMapper.writeValueAsString(content), Set.of());
    }

    public static String getMaskingForObject(Object content, Set<String> needMaskingKeys) {
        return processJsonString(customObjectMapper.writeValueAsString(content), needMaskingKeys);
    }

    private static String processJsonString(String content, Set<String> needMaskingKeys) {
        try {
            JsonNode rootJsonNode = customObjectMapper.readTree(content);
            if (rootJsonNode.isObject()) {
                TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
                };
                return Optional.ofNullable(customObjectMapper.readValue(content, typeReference))
                    .map(jsonObject -> maskingJsonObject(jsonObject, needMaskingKeys))
                    .map(customObjectMapper::writeValueAsString)
                    .orElse(content);
            } else if (rootJsonNode.isArray()) {
                TypeReference<List<Object>> typeReference = new TypeReference<>() {
                };
                return Optional.ofNullable(customObjectMapper.readValue(content, typeReference))
                    .map(jsonArray -> maskingJsonArray(jsonArray, null, needMaskingKeys))
                    .map(customObjectMapper::writeValueAsString)
                    .orElse(content);
            } else {
                return content;
            }
        } catch (JsonProcessingException e) {
            log.debug("Ошибка десериализации:", e);
            return content;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> maskingJsonObject(Map<String, Object> mapToMask, Set<String> needMaskingKeys) {

        Map<String, Object> maskedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Object> jsonPair : mapToMask.entrySet()) {
            if (jsonPair.getValue() instanceof Map) {
                maskedMap.put(
                    jsonPair.getKey(),
                    maskingJsonObject((Map<String, Object>) jsonPair.getValue(), needMaskingKeys)
                );
            } else if (jsonPair.getValue() instanceof List<?>) {
                maskedMap.put(
                    jsonPair.getKey(),
                    maskingJsonArray((List<Object>) jsonPair.getValue(), jsonPair.getKey(), needMaskingKeys)
                );
            } else if (jsonPair.getKey() != null
                && (needMaskingKeys.isEmpty() || needMaskingKeys.contains(jsonPair.getKey()))) {
                maskedMap.put(
                    jsonPair.getKey(),
                    maskString(String.valueOf(jsonPair.getValue()))
                );
            } else {
                maskedMap.put(jsonPair.getKey(), jsonPair.getValue());
            }
        }
        return maskedMap;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> maskingJsonArray(List<Object> listToMask, String key, Set<String> needMaskingKeys) {

        List<Object> maskedList = new ArrayList<>();

        for (Object jsonFromArray : listToMask) {
            if (jsonFromArray instanceof Map<?, ?>) {
                maskedList.add(maskingJsonObject((Map<String, Object>) jsonFromArray, needMaskingKeys));
            } else if (key != null
                && (needMaskingKeys.isEmpty() || needMaskingKeys.contains(key))) {
                maskedList.add(maskString(String.valueOf(jsonFromArray)));
            } else {
                maskedList.add(String.valueOf(jsonFromArray));
            }
        }
        return maskedList;
    }
}
