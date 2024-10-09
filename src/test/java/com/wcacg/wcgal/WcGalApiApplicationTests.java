package com.wcacg.wcgal;

import com.wcacg.wcgal.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WcGalApiApplicationTests {
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {

    }

}
