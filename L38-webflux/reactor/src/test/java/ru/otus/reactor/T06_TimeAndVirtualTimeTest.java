package ru.otus.reactor;

import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Многие реактивные потоки завязаны на время: delay, interval, timeout.
 * Ждать реальные секунды/минуты в тестах — плохая идея, поэтому reactor-test
 * умеет подменять время виртуальным планировщиком через StepVerifier.withVirtualTime.
 *
 * <p>Важно: Publisher нужно создавать ВНУТРИ лямбды-supplier, а не заранее —
 * иначе виртуальный планировщик ещё не будет активен на момент его сборки.
 */
class T06_TimeAndVirtualTimeTest {

    @Test
    @DisplayName("Mono.delay — эмитит одно значение (тик) спустя заданное время")
    void monoDelayWithVirtualTime() {
        StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofMinutes(1)).thenReturn("done"))
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(59))
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("done")
                .verifyComplete();
    }

    @Test
    @DisplayName("Flux.interval — бесконечный поток тиков с заданным периодом")
    void fluxIntervalWithVirtualTime() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(1)).take(3))
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    @DisplayName("timeout — если значение не пришло вовремя, поток завершается ошибкой TimeoutException")
    void timeoutOperator() {
        // Mono.never() никогда не эмитит и не завершается сам по себе — идеальный кандидат
        // на демонстрацию timeout
        StepVerifier.withVirtualTime(() -> Mono.never().timeout(Duration.ofSeconds(5)))
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(5))
                .expectError(TimeoutException.class)
                .verify();
    }

    @Test
    @DisplayName("delayElements — задержка перед каждым элементом потока, а не только перед первым")
    void delayElements() {
        StepVerifier.withVirtualTime(() -> Flux.just("a", "b", "c").delayElements(Duration.ofSeconds(10)))
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(30))
                .expectNext("a", "b", "c")
                .verifyComplete();
    }
}
