package com.zadziarnouski.habitordie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class HabitOrDieApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitOrDieApplication.class, args);
    }

}
