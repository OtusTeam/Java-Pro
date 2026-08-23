package ru.otus.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

/**
 * Backpressure (обратное давление) — механизм Reactive Streams, при котором подписчик
 * сам явно запрашивает у источника, сколько элементов готов принять (request(n)),
 * а не источник насильно "заливает" его данными. Это ключевое отличие Reactive Streams
 * от, например, обычных java.util.Observer/Observable коллбэков.
 */
class T08_BackpressureTest {

    @Test
    @DisplayName("StepVerifier с ограниченным начальным спросом: элементы приходят порциями по request(n)")
    void controlledDemandWithStepVerifier() {
        StepVerifier.create(Flux.range(1, 5), 0) // подписываемся с запросом 0 элементов
                .expectSubscription()
                .thenRequest(2)
                .expectNext(1, 2)
                .thenRequest(3)
                .expectNext(3, 4, 5)
                .verifyComplete();
    }

    @Test
    @DisplayName("Свой BaseSubscriber, запрашивающий элементы по одному")
    void customSubscriberRequestingOneByOne() {
        List<Integer> received = new ArrayList<>();

        Flux.range(1, 5).subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {
                received.add(value);
                request(1); // запрашиваем следующий элемент только после обработки текущего
            }
        });

        assertThat(received).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    @DisplayName("onBackpressureDrop: элементы, которые никто не запрашивал, отбрасываются без ошибки")
    void onBackpressureDrop() {
        Flux<Integer> source = Flux.create(sink -> {
            for (int i = 1; i <= 5; i++) {
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.DROP);

        StepVerifier.create(source, 0)
                .expectSubscription()
                .thenRequest(2)
                // запросили только 2 элемента — 3, 4 и 5 будут отброшены стратегией DROP,
                // а не поставлены в очередь на потом
                .expectNext(1, 2)
                .verifyComplete();
    }
}
