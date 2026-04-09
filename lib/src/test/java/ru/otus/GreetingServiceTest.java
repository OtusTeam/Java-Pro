package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Шаблон тестов по мотивам лекции про unit-тесты.
 *
 * ВАЖНО:
 * 1. Здесь предполагается, что в lib есть класс GreetingService
 * 2. И что у него есть метод greet(String name): String
 *
 * Если сигнатура у вас отличается, поменяйте только создание sut и вызовы метода.
 */
class GreetingServiceTest {

    private final GreetingService sut = new GreetingService();

    @Test
    @DisplayName("Должен возвращать приветствие для обычного имени")
    void shouldReturnGreetingForRegularName() {
        String actual = sut.greet("Valentin");

        assertNotNull(actual);
        assertTrue(actual.contains("Valentin"),
                "В приветствии должно присутствовать переданное имя");
    }

    @Test
    @DisplayName("Должен корректно работать с кириллицей")
    void shouldSupportCyrillicName() {
        String actual = sut.greet("Валентин");

        assertNotNull(actual);
        assertTrue(actual.contains("Валентин"),
                "Сервис должен поддерживать кириллицу в имени");
    }

    @Test
    @DisplayName("Не должен возвращать пустую строку")
    void shouldNotReturnBlankGreeting() {
        String actual = sut.greet("Otus");

        assertNotNull(actual);
        assertFalse(actual.isBlank(),
                "Приветствие не должно быть пустым или состоять только из пробелов");
    }

    @Test
    @DisplayName("Должен обрезать лишние пробелы по краям имени")
    void shouldTrimName() {
        String actual = sut.greet("  Alex  ");

        assertNotNull(actual);
        assertTrue(actual.contains("Alex"),
                "После нормализации имя должно попадать в результат без крайних пробелов");
        assertFalse(actual.contains("  Alex  "),
                "Сырым значением с лишними пробелами лучше не пользоваться");
    }

    @Test
    @DisplayName("Должен предсказуемо обрабатывать пустое имя")
    void shouldHandleBlankName() {
        assertDoesNotThrow(() -> sut.greet(""),
                "Даже для пустого имени сервис не должен падать");

        String actual = sut.greet("");
        assertNotNull(actual);
    }
}
