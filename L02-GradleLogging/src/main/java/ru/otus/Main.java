package ru.otus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger log =
            LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        GreetingService service = new GreetingService();
        System.out.println(service.greet("test"));

        log.info("Application started");
        int userId = 42;
        log.debug("User id = {}", userId);
        try {
            simulateError();
        } catch (Exception e) {
            log.error("Error occured: {}", e.getMessage(), e);
        }
        log.warn("Applciation finished with warning");



    }

    private static void simulateError() {
        throw new RuntimeException("Test exception");
    }
}