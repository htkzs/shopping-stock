package com.itheima.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class CubemallStockApplication {
    public static void main(String[] args) {
        SpringApplication.run(CubemallStockApplication.class, args);
    }
}
