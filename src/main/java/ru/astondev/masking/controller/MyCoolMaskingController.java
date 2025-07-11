package ru.astondev.masking.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.astondev.masking.dto.MySensitiveDto;
import ru.astondev.masking.maskingv1.MaskingObjectsV1;

import java.time.LocalDate;
import java.util.Set;

import static ru.astondev.masking.maskingv1.CustomObjectMapper.OBJECT_MAPPER;
import static ru.astondev.masking.maskingv2.MaskingObjectMapper.SENSITIVE_OBJECT_MAPPER;

@Slf4j
@RestController
public class MyCoolMaskingController {

    @GetMapping("/action")
    public MySensitiveDto getMyCoolMasking() throws JsonProcessingException {

        var response = new MySensitiveDto(
            123456L,
            "VERY_SENSITIVE_INFO",
            LocalDate.now()
        );

        log.info("\n");
        log.info("Kirov Reporting: {}", response);
        log.info("\n");
        log.info("---------------------------JSON_NODE_MASKING----------------------------------");
        log.info("Json_node_masking {}", MaskingObjectsV1.getMaskingForObject(response, Set.of("accountNumber", "signDate")));
        log.info("\n");
        log.info("---------------------------JSON_OBJECT_MAPPER_MASKING----------------------------------");
        log.info("JsonObject serialization masking: {}", SENSITIVE_OBJECT_MAPPER.writeValueAsString(response));
        log.info("\n");
        log.info("---------------------------SL4J2_PLUGIN_MASKING----------------------------------");
        log.info("JsonObject serialization masking: {}", OBJECT_MAPPER.writeValueAsString(response));
        log.info("\n");

        return response;
    }
}
