package ru.otus.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Холодный (cold) Publisher заново проигрывает всю последовательность для каждого
 * нового подписчика (как видео с YouTube, запущенное с начала для каждого зрителя).
 *
 * <p>Горячий (hot) Publisher существует независимо от подписчиков и просто рассылает
 * им те события, что происходят "прямо сейчас" (как прямой эфир — подключившийся
 * зритель не увидит то, что уже показали раньше).
 *
 * <p>Sinks.Many — программный способ вручную эмитить значения в горячий поток
 * (например, при мосте между внешним callback-API и Reactor).
 */
class T07_ColdVsHotPublisherTest {

    @Test
    @DisplayName("Холодный Flux: каждый подписчик заново запускает источник и получает все элементы")
    void coldPublisherReplaysForEverySubscriber() {
        AtomicInteger sourceRuns = new AtomicInteger();
        Flux<Integer> cold = Flux.defer(() -> {
            sourceRuns.incrementAndGet();
            return Flux.range(1, 3);
        });

        List<Integer> first = cold.collectList().block();
        List<Integer> second = cold.collectList().block();

        assertThat(sourceRuns.get()).isEqualTo(2);
        assertThat(first).containsExactly(1, 2, 3);
        assertThat(second).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("Горячий поток на Sinks.Many: подписчик, пришедший позже, пропускает уже отправленные события")
    void hotPublisherMulticastSkipsPastEvents() {
        Sinks.Many<Integer> sink = Sinks.many().multicast().onBackpressureBuffer();
        Flux<Integer> hotFlux = sink.asFlux();

        List<Integer> earlySubscriber = new CopyOnWriteArrayList<>();
        hotFlux.subscribe(earlySubscriber::add);

        sink.tryEmitNext(1);
        sink.tryEmitNext(2);

        List<Integer> lateSubscriber = new CopyOnWriteArrayList<>();
        hotFlux.subscribe(lateSubscriber::add);

        sink.tryEmitNext(3);
        sink.tryEmitComplete();

        assertThat(earlySubscriber).containsExactly(1, 2, 3);
        // подписался после 1 и 2 — их уже не увидит, только то, что было отправлено после подписки
        assertThat(lateSubscriber).containsExactly(3);
    }

    @Test
    @DisplayName("Sinks.many().replay(): поздний подписчик получает буфер ранее отправленных значений")
    void replaySinkKeepsHistoryForLateSubscribers() {
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        replaySink.tryEmitNext(1);
        replaySink.tryEmitNext(2);
        replaySink.tryEmitNext(3);
        replaySink.tryEmitComplete();

        // подписываемся уже после того, как все значения отправлены и поток завершён
        List<Integer> lateSubscriber = replaySink.asFlux().collectList().block();

        assertThat(lateSubscriber).containsExactly(1, 2, 3);
    }
}
