package com.wjy.rsmapping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.wjy.rsmapping"})
@MapperScan(basePackages = {"com.wjy.rsmapping.mapper"})
public class RsmappingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsmappingApplication.class, args);
    }

}
