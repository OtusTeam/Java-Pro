package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// http://localhost:8080
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(Main.class, args);
        System.out.println(ctx.getBeanDefinitionCount());
    }
}
