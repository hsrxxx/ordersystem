package com.hsrxxx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: ordersystem
 * @description:
 * @author: hsrxxx
 * @create: 2021-03-08 10:30
 **/
@SpringBootApplication
@MapperScan("com.hsrxxx.mapper")
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
