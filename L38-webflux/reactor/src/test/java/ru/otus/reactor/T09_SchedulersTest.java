package ru.otus.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Reactor не создаёт потоки сам по себе — по умолчанию весь конвейер выполняется
 * на потоке подписчика. Управлять тем, на каком Scheduler (пуле потоков) выполняется
 * та или иная часть цепочки, позволяют операторы subscribeOn и publishOn.
 *
 * <p>subscribeOn — переключает поток, на котором происходит ПОДПИСКА и выполняется
 * сам источник, независимо от места в цепочке, где он указан.
 * <p>publishOn — переключает поток для всех операторов НИЖЕ по цепочке, начиная
 * с этой точки.
 */
class T09_SchedulersTest {

    @Test
    @DisplayName("subscribeOn переносит выполнение всего источника на указанный Scheduler")
    void subscribeOnMovesSubscriptionThread() {
        List<String> threadNames = new CopyOnWriteArrayList<>();

        Flux.range(1, 3)
                .doOnNext(i -> threadNames.add(Thread.currentThread().getName()))
                .subscribeOn(Schedulers.boundedElastic())
                .blockLast(Duration.ofSeconds(5));

        assertThat(threadNames).hasSize(3);
        assertThat(threadNames).allMatch(name -> name.startsWith("boundedElastic"));
    }

    @Test
    @DisplayName("publishOn переключает поток только для операторов ниже по цепочке")
    void publishOnAffectsOnlyDownstream() {
        List<String> beforeSwitch = Collections.synchronizedList(new java.util.ArrayList<>());
        List<String> afterSwitch = Collections.synchronizedList(new java.util.ArrayList<>());

        Flux.range(1, 3)
                .doOnNext(i -> beforeSwitch.add(Thread.currentThread().getName()))
                .publishOn(Schedulers.parallel())
                .doOnNext(i -> afterSwitch.add(Thread.currentThread().getName()))
                .blockLast(Duration.ofSeconds(5));

        String callingThread = Thread.currentThread().getName();

        // до publishOn всё выполняется на потоке, вызвавшем subscribe (потоке теста)
        assertThat(beforeSwitch).allMatch(name -> name.equals(callingThread));
        // после publishOn — уже на потоке из parallel-пула
        assertThat(afterSwitch).allMatch(name -> name.startsWith("parallel"));
    }

    @Test
    @DisplayName("parallel()/runOn — распараллеливание обработки элементов между несколькими потоками")
    void parallelFlux() {
        List<Integer> result = Flux.range(1, 10)
                .parallel(4)
                .runOn(Schedulers.parallel())
                .map(i -> i * i)
                .sequential()
                .collectSortedList()
                .block(Duration.ofSeconds(5));

        assertThat(result).containsExactly(1, 4, 9, 16, 25, 36, 49, 64, 81, 100);
    }
}
