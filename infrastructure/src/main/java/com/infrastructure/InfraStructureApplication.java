package com.infrastructure;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(value = "com.infrastructure")
@EnableJpaRepositories
@EnableRabbit
public class InfraStructureApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfraStructureApplication.class);
    }
}
