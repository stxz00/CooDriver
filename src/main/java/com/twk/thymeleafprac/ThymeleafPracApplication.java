package com.twk.thymeleafprac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.twk.thymeleafprac.mapper")
public class ThymeleafPracApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThymeleafPracApplication.class, args);
    }

}
