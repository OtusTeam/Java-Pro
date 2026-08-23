package ru.otus.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

/**
 * Reactor Context — механизм передачи данных вдоль цепочки операторов без явных
 * параметров метода (аналог ThreadLocal, но безопасный для асинхронного/многопоточного
 * выполнения, так как контекст привязан к конкретной подписке, а не к потоку).
 *
 * <p>Ключевая особенность: контекст читается "снизу вверх" — от subscribe() к источнику.
 * Поэтому contextWrite должен стоять НИЖЕ по цепочке (ближе к подписке), чем операторы,
 * которые этот контекст читают.
 *
 * <p>Sinks — программный API для ручной генерации сигналов (аналог "promise"/"future",
 * но для реактивных потоков): удобен, когда нужно связать Reactor с внешним callback-API.
 */
class T10_ContextAndSinksTest {

    @Test
    @DisplayName("Context.contextWrite должен стоять ниже по цепочке, чем чтение контекста")
    void contextIsReadFromDownstreamToUpstream() {
        Mono<String> mono = Mono.deferContextual(ctx -> Mono.just(ctx.get("user")))
                .map(user -> "Hello, " + user + "!")
                .contextWrite(Context.of("user", "Otus"));

        StepVerifier.create(mono)
                .expectNext("Hello, Otus!")
                .verifyComplete();
    }

    @Test
    @DisplayName("Контекст изолирован по подписке: один и тот же Mono даёт разный результат для разных подписчиков")
    void contextIsIsolatedPerSubscription() {
        Mono<String> template = Mono.deferContextual(ctx -> Mono.just("user=" + ctx.get("user")));

        StepVerifier.create(template.contextWrite(Context.of("user", "alice")))
                .expectNext("user=alice")
                .verifyComplete();

        StepVerifier.create(template.contextWrite(Context.of("user", "bob")))
                .expectNext("user=bob")
                .verifyComplete();
    }

    @Test
    @DisplayName("Sinks.One — мост между императивным кодом и Mono, как reactive-версия CompletableFuture")
    void sinksOneBridgesImperativeCodeToMono() {
        Sinks.One<String> sink = Sinks.one();

        // где-то в императивном коде (например, в колбэке внешнего SDK) результат становится известен...
        sink.tryEmitValue("result from callback");

        // ...и подписчики Mono получают его обычным реактивным способом
        StepVerifier.create(sink.asMono())
                .expectNext("result from callback")
                .verifyComplete();
    }

    @Test
    @DisplayName("Sinks.Many.unicast — буферизует значения, отправленные до появления подписчика")
    void unicastSinkBuffersUntilSubscribed() {
        Sinks.Many<Integer> sink = Sinks.many().unicast().onBackpressureBuffer();

        sink.tryEmitNext(1);
        sink.tryEmitNext(2);
        sink.tryEmitComplete();

        // подписываемся уже после того, как все значения отправлены —
        // unicast-синк хранит их в буфере до момента подписки
        StepVerifier.create(sink.asFlux())
                .expectNext(1, 2)
                .verifyComplete();
    }
}
