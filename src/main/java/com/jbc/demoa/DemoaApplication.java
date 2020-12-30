package com.jbc.demoa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.jbc.demoa.mapper")
public class DemoaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoaApplication.class, args);
    }

}
