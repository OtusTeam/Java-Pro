package ru.otus.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Базовые синхронные операторы преобразования потока: map, filter, distinct,
 * take/skip, reduce, count. Все они не меняют "природу" источника (холодный
 * остаётся холодным), а лишь описывают дополнительный шаг обработки элемента.
 */
class T03_OperatorsMapFilterTest {

    @Test
    @DisplayName("map — преобразование каждого элемента")
    void map() {
        StepVerifier.create(Flux.just(1, 2, 3).map(i -> i * 10))
                .expectNext(10, 20, 30)
                .verifyComplete();
    }

    @Test
    @DisplayName("filter — пропускаем только элементы, удовлетворяющие условию")
    void filter() {
        StepVerifier.create(Flux.range(1, 10).filter(i -> i % 2 == 0))
                .expectNext(2, 4, 6, 8, 10)
                .verifyComplete();
    }

    @Test
    @DisplayName("distinct, take, skip — работа с последовательностью как с коллекцией")
    void distinctTakeSkip() {
        StepVerifier.create(Flux.just(1, 1, 2, 2, 3, 3, 4, 5).distinct())
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();

        StepVerifier.create(Flux.range(1, 100).take(3))
                .expectNext(1, 2, 3)
                .verifyComplete();

        StepVerifier.create(Flux.range(1, 5).skip(3))
                .expectNext(4, 5)
                .verifyComplete();
    }

    @Test
    @DisplayName("reduce — свёртка потока в одно значение (Mono)")
    void reduce() {
        Mono<Integer> sum = Flux.range(1, 5).reduce(0, Integer::sum);

        StepVerifier.create(sum)
                .expectNext(15)
                .verifyComplete();
    }

    @Test
    @DisplayName("count — количество элементов в потоке, тоже приходит как Mono<Long>")
    void count() {
        StepVerifier.create(Flux.just("a", "b", "c").count())
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    @DisplayName("map на Mono работает так же, как на Flux, только для одного значения")
    void mapOnMono() {
        StepVerifier.create(Mono.just("otus").map(String::toUpperCase))
                .expectNext("OTUS")
                .verifyComplete();
    }
}
