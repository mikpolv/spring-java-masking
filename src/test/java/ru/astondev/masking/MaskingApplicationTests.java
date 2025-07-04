package ru.astondev.masking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


class MaskingApplicationTests {

    @Test
    void contextLoads() {
        Boolean b = null;
        if (b) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }

}
