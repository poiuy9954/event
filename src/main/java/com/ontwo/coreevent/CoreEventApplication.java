package com.ontwo.coreevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class CoreEventApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CoreEventApplication.class);
        application.setDefaultProperties(Map.of(
                "spring.config.name", "coreEventApp"
        ));
        application.run(args);
    }

}
