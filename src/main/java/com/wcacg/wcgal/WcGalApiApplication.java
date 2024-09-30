package com.wcacg.wcgal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class WcGalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WcGalApiApplication.class, args);
    }

}
