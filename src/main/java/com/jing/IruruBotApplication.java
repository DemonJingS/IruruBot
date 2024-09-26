package com.jing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.jing.mapper")
@SpringBootApplication
public class IruruBotApplication {

    public static void main(String[] args) {
        //代理
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "7890");
        SpringApplication.run(IruruBotApplication.class, args);
    }

}
