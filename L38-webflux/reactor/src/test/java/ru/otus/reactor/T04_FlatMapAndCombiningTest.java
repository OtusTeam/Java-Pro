package ru.otus.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * flatMap/concatMap разворачивают элемент в новый Publisher (Mono/Flux) и "склеивают"
 * результаты в один поток — в отличие от map, который просто трансформирует значение
 * без изменения формы потока.
 *
 * <p>Здесь же — операторы объединения нескольких источников: zip, merge, concat.
 */
class T04_FlatMapAndCombiningTest {

    @Test
    @DisplayName("map возвращает T -> R, flatMap возвращает T -> Publisher<R>")
    void mapVsFlatMap() {
        // map: 1 элемент на входе -> 1 элемент на выходе
        StepVerifier.create(Flux.just(1, 2, 3).map(i -> i * i))
                .expectNext(1, 4, 9)
                .verifyComplete();

        // flatMap: 1 элемент на входе -> произвольное число элементов на выходе,
        // здесь каждое число "разворачивается" в диапазон [1..n]
        StepVerifier.create(Flux.just(2, 3).flatMap(n -> Flux.range(1, n)))
                .expectNextCount(2 + 3)
                .verifyComplete();
    }

    @Test
    @DisplayName("flatMap выполняет вложенные Publisher-ы конкурентно и не гарантирует порядок")
    void flatMapReordersByCompletionTime() {
        // чем короче задержка, тем раньше элемент завершится и попадёт в результат
        List<Integer> result = Flux.just(300, 100, 10)
                .flatMap(delayMs -> Mono.just(delayMs).delayElement(Duration.ofMillis(delayMs)))
                .collectList()
                .block(Duration.ofSeconds(5));

        assertThat(result).containsExactly(10, 100, 300);
    }

    @Test
    @DisplayName("concatMap выполняет вложенные Publisher-ы строго последовательно, порядок сохраняется")
    void concatMapPreservesOrder() {
        List<Integer> result = Flux.just(300, 100, 10)
                .concatMap(delayMs -> Mono.just(delayMs).delayElement(Duration.ofMillis(delayMs)))
                .collectList()
                .block(Duration.ofSeconds(5));

        assertThat(result).containsExactly(300, 100, 10);
    }

    @Test
    @DisplayName("Flux.zip — попарное объединение элементов из нескольких источников по индексу")
    void zip() {
        Flux<String> zipped = Flux.zip(
                Flux.just(1, 2, 3),
                Flux.just("a", "b", "c"),
                (number, letter) -> number + letter);

        StepVerifier.create(zipped)
                .expectNext("1a", "2b", "3c")
                .verifyComplete();
    }

    @Test
    @DisplayName("Flux.concat — сначала полностью первый источник, потом второй")
    void concat() {
        Flux<Integer> concatenated = Flux.concat(Flux.just(1, 2), Flux.just(3, 4));

        StepVerifier.create(concatenated)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    @DisplayName("Flux.merge — источники подписываются одновременно, порядок между ними не гарантирован")
    void merge() {
        List<Integer> result = Flux.merge(Flux.just(1, 2), Flux.just(3, 4))
                .collectList()
                .block();

        assertThat(result).containsExactlyInAnyOrder(1, 2, 3, 4);
    }
}
