package ru.otus;

public class GreetingService {

    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, Anonymous";
        }

        String normalized = name.trim();

        return "Hello, " + normalized;
    }
}