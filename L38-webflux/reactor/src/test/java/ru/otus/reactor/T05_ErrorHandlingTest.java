package ru.otus.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * В реактивных потоках ошибка — это терминальный сигнал (onError), после которого
 * поток больше не эмитит элементов, даже если "физически" источник мог бы продолжать.
 * Поэтому обработка ошибок делается специальными операторами, а не try/catch.
 */
class T05_ErrorHandlingTest {

    @Test
    @DisplayName("onErrorReturn — подменяем ошибку на запасное значение")
    void onErrorReturn() {
        Mono<String> withFallback = Mono.<String>error(new RuntimeException("failed"))
                .onErrorReturn("fallback");

        StepVerifier.create(withFallback)
                .expectNext("fallback")
                .verifyComplete();
    }

    @Test
    @DisplayName("onErrorResume — подменяем ошибку на альтернативный Publisher")
    void onErrorResume() {
        Flux<String> primary = Flux.just("a", "b")
                .concatWith(Flux.error(new IOException("network down")))
                .onErrorResume(e -> Flux.just("recovered-" + e.getMessage()));

        StepVerifier.create(primary)
                .expectNext("a", "b", "recovered-network down")
                .verifyComplete();
    }

    @Test
    @DisplayName("onErrorMap — превращаем одно исключение в другое, не теряя семантику ошибки")
    void onErrorMap() {
        Mono<String> mono = Mono.<String>error(new IllegalArgumentException("bad input"))
                .onErrorMap(IllegalArgumentException.class, e -> new IllegalStateException("wrapped: " + e.getMessage()));

        StepVerifier.create(mono)
                .expectErrorMatches(e -> e instanceof IllegalStateException
                        && e.getMessage().equals("wrapped: bad input"))
                .verify();
    }

    @Test
    @DisplayName("doOnError — побочный эффект (например, логирование) без изменения сигнала ошибки")
    void doOnError() {
        AtomicInteger errorLogCount = new AtomicInteger();

        Mono<String> mono = Mono.<String>error(new RuntimeException("boom"))
                .doOnError(e -> errorLogCount.incrementAndGet());

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();

        assertThat(errorLogCount.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("retry — повторная подписка на источник при ошибке")
    void retry() {
        AtomicInteger attempts = new AtomicInteger();

        Mono<String> flaky = Mono.fromCallable(() -> {
                    int attempt = attempts.incrementAndGet();
                    if (attempt < 3) {
                        throw new RuntimeException("attempt " + attempt + " failed");
                    }
                    return "success on attempt " + attempt;
                })
                .retry(2);

        StepVerifier.create(flaky)
                .expectNext("success on attempt 3")
                .verifyComplete();

        assertThat(attempts.get()).isEqualTo(3);
    }
}
