package com.valleon.pakamapp;

import com.valleon.pakamapp.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SecurityConfig.class)
@SpringBootApplication
public class PakamAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PakamAppApplication.class, args);
    }

}
