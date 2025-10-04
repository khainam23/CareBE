package com.careservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CareBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareBookingSystemApplication.class, args);
    }
}
