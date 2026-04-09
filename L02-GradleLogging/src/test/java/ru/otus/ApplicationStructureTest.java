package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Небольшой smoke-тест структуры приложения.
 * Подходит как 5-й тест "на проект", если хотите проверить базовую договоренность по точке входа.
 */
class ApplicationStructureTest {

    @Test
    @DisplayName("Класс Main должен содержать точку входа main")
    void mainClassShouldContainEntryPoint() throws Exception {
        Method method = Main.class.getMethod("main", String[].class);

        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());



    }
}
