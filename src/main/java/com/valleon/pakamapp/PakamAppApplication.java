package com.valleon.pakamapp;


import com.valleon.pakamapp.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Import(SecurityConfig.class)
@SpringBootApplication

public class PakamAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PakamAppApplication.class, args);
    }

}
