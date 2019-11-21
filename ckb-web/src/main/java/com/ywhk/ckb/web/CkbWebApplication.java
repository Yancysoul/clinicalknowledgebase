package com.ywhk.ckb.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ywhk.ckb.dao.repository")
@EntityScan(basePackages = "com.ywhk.ckb.dao.model")
@ComponentScan(basePackages = {"com.ywhk.ckb"})
public class CkbWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CkbWebApplication.class, args);
    }

}
