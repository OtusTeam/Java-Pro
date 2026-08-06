package ru.demo;

import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorDemo {
    private static final Logger log = LoggerFactory.getLogger(ExecutorDemo.class);

    public static void main(String[] args) {
        var factory = Thread.ofVirtual().name("routine-", 0).factory();
        try (var executor = Executors.newFixedThreadPool(10, factory)) {
            for (int i = 0; i < 1000; i++) {
                var no = i;
                executor.submit(() -> log.info("task-{}, thread:{}", no, Thread.currentThread()));
            }
        }
    }
}
