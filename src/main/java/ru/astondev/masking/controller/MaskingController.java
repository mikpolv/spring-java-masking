package ru.astondev.masking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.astondev.masking.dto.VerySensitiveDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static ru.astondev.masking.utils.CustomObjectMapper.OBJECT_MAPPER;
import static ru.astondev.masking.utils.CustomObjectMapper.OBJECT_SENSITIVE_MAPPER;
import static ru.astondev.masking.utils.MaskingObjectsV1.getMaskingForObject;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MaskingController {


    @GetMapping("/string")
    public VerySensitiveDto mask() throws JsonProcessingException {

        var response = new VerySensitiveDto(
            12378278934L,
            "50003230002300012345",
            LocalDate.now(),
            "маскирование",
            BigDecimal.valueOf(1_000_000_000L)
        );

        log.info("Kirov Reporting {}", response); // что то вывести, лишь бы отладить

        log.info("Log json: {}", OBJECT_MAPPER.writeValueAsString(response)); // как то стандартизировать вывод
        log.info("Log masked json: {}", getMaskingForObject(response)); // Требования ИБ - маскировать
        log.info("Log masked json: {}",
            getMaskingForObject(response, Set.of("accountNumber", "sensitiveInfo", "sensitiveDate"))); //
        // Требования поддержки/здравого смысла - айдишники верни
        log.info("Log json: {}", OBJECT_SENSITIVE_MAPPER.writeValueAsString(response)); // Маскирование с помощью
        // jackson

        // next @sensitive -> посчитали звездочки
        //

        return response;
    }
}
