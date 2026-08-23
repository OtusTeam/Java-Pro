package ru.otus.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Mono&lt;T&gt; — реактивный источник, который эмитит максимум одно значение
 * и завершается (onNext + onComplete), либо завершается пустым (только onComplete),
 * либо завершается ошибкой (onError).
 *
 * <p>С этого класса начинается знакомство с Project Reactor: как создать Mono,
 * почему он ничего не делает без подписки, и как получить значение синхронно (block)
 * или асинхронно (subscribe).
 */
class T01_MonoBasicsTest {

    @Test
    @DisplayName("Mono.just — источник с одним заранее известным значением")
    void monoJust() {
        Mono<String> mono = Mono.just("hello");

        // block() синхронно дожидается результата — удобно в тестах,
        // но в реальном реактивном коде (например, в WebFlux-хендлере) так делать нельзя,
        // так как это блокирует поток и убивает весь смысл неблокирующего стека.
        String value = mono.block();

        assertThat(value).isEqualTo("hello");
    }

    @Test
    @DisplayName("Mono.empty и Mono.justOrEmpty — пустой результат без ошибки")
    void monoEmpty() {
        assertThat(Mono.empty().blockOptional()).isEmpty();
        assertThat(Mono.justOrEmpty(null).blockOptional()).isEmpty();
        assertThat(Mono.justOrEmpty("value").blockOptional()).contains("value");
    }

    @Test
    @DisplayName("Mono ленив: код внутри него не выполняется, пока никто не подписался")
    void monoIsLazy() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Mono<String> mono = Mono.fromSupplier(() -> {
            executed.set(true);
            return "computed";
        });

        // Mono только описан, но ещё не запущен
        assertThat(executed).isFalse();

        mono.subscribe();

        // а вот теперь, после подписки, поставщик значения реально вызвался
        assertThat(executed).isTrue();
    }

    @Test
    @DisplayName("subscribe с колбэками — асинхронный способ получить значение")
    void subscribeWithCallback() {
        StringBuilder result = new StringBuilder();

        Mono.just("async value").subscribe(result::append);

        // Mono.just работает синхронно "здесь и сейчас" (без операторов времени/потоков),
        // поэтому colbek уже отработал к этому моменту.
        assertThat(result.toString()).isEqualTo("async value");
    }

    @Test
    @DisplayName("StepVerifier — основной инструмент для тестирования реактивных потоков")
    void stepVerifierBasics() {
        StepVerifier.create(Mono.just(42))
                .expectNext(42)
                .verifyComplete();

        StepVerifier.create(Mono.empty())
                .verifyComplete();
    }

    @Test
    @DisplayName("Mono.error — источник, который сразу завершается ошибкой")
    void monoError() {
        Mono<String> failing = Mono.error(new IllegalStateException("boom"));

        StepVerifier.create(failing)
                .expectErrorMessage("boom")
                .verify();
    }
}
