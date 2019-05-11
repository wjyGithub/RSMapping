package com.wjy.rsmapping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.wjy.rsmapping"})
public class RsmappingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsmappingApplication.class, args);
    }

}
