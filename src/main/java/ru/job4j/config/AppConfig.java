package ru.job4j.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${telegram.bot.name}")
    private String name;

    public void printConfig() {
        System.out.println(name);
    }
}
