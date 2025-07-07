package ru.astondev.masking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.astondev.masking.dto.VerySensitiveDto;

import java.time.LocalDate;
import java.util.Set;

import static ru.astondev.masking.maskingv1.CustomObjectMapper.OBJECT_MAPPER;
import static ru.astondev.masking.maskingv1.MaskingObjectsV1.getMaskingForObject;
import static ru.astondev.masking.maskingv2.MaskingObjectMapper.OBJECT_SENSITIVE_MAPPER;

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
            "маскирование"
        );

        log.info("Kirov Reporting {}\n", response); // что-то вывести для отладки
        // принимать командные решения для логгирования (язык форма)

        log.info("=========================default==============================");
        log.info("Log json: {}\n", OBJECT_MAPPER.writeValueAsString(response)); // как то стандартизировать вывод

        log.info("=========================Masked jsonNode all===============================");
        log.info("Log masked json: {}\n", getMaskingForObject(response)); // Требования ИБ - маскировать

        log.info("=========================Masked jsonNode blacklist===============================");
        log.info("Log masked json: {}\n",
            getMaskingForObject(response, Set.of("accountNumber", "sensitiveInfo", "sensitiveDate"))); //
        // Требования поддержки/здравого смысла - айдишники верни

        log.info("Log json: {}\n", OBJECT_SENSITIVE_MAPPER.writeValueAsString(response)); // Маскирование с помощью
        // jackson
        log.warn("+++++++++++++++++++++++FINISH+++++++++++++++++++++HIM+\n");
        return response;
    }
}
