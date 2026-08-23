package ru.otus.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Flux&lt;T&gt; — реактивный источник из 0..N элементов, который завершается
 * успехом (onComplete) или ошибкой (onError). Это аналог Mono, но для потока значений.
 */
class T02_FluxBasicsTest {

    @Test
    @DisplayName("Flux.just / Flux.fromIterable / Flux.range — базовые способы создания")
    void creatingFlux() {
        StepVerifier.create(Flux.just("a", "b", "c"))
                .expectNext("a", "b", "c")
                .verifyComplete();

        StepVerifier.create(Flux.fromIterable(List.of(1, 2, 3)))
                .expectNext(1, 2, 3)
                .verifyComplete();

        StepVerifier.create(Flux.range(10, 3))
                .expectNext(10, 11, 12)
                .verifyComplete();
    }

    @Test
    @DisplayName("collectList — собрать весь Flux в один Mono<List<T>>")
    void collectList() {
        List<Integer> result = Flux.range(1, 5).collectList().block();

        assertThat(result).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    @DisplayName("Flux холоден: у каждого нового подписчика — своя независимая последовательность")
    void fluxIsColdByDefault() {
        AtomicInteger subscriptions = new AtomicInteger();
        Flux<Integer> cold = Flux.defer(() -> {
            subscriptions.incrementAndGet();
            return Flux.just(1, 2, 3);
        });

        // источник ещё не создавался ни разу
        assertThat(subscriptions.get()).isZero();

        List<Integer> first = cold.collectList().block();
        List<Integer> second = cold.collectList().block();

        // каждый subscribe() заново прогоняет всю последовательность с начала
        assertThat(subscriptions.get()).isEqualTo(2);
        assertThat(first).isEqualTo(second).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("StepVerifier.expectNextCount — проверка количества элементов без перечисления каждого")
    void expectNextCount() {
        StepVerifier.create(Flux.range(1, 100))
                .expectNextCount(100)
                .verifyComplete();
    }
}
