package ru.astondev.masking.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.astondev.masking.dto.MySensitiveDto;

@Slf4j
@RestController
public class MyCoolMaskingController {

    @GetMapping("/action")
    public MySensitiveDto getMyCoolMasking() throws JsonProcessingException {
        return null;
    }
}
