package ru.isit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IsitApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsitApplication.class, args);
    }

}
