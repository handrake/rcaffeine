package com.github.handrake.rcaffeine.cache.example;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.handrake.rcaffeine.cache.ReactiveCache;
import com.github.handrake.rcaffeine.cache.ReactiveCacheBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReactiveCacheTest {
    private ReactiveCache cache;

    @BeforeEach
    void setUp() {
        Caffeine cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000);

        ReactiveCacheBuilder builder = new ReactiveCacheBuilder(cache);

        Executor executor = Executors.newFixedThreadPool(10);

        builder.executor(executor);

        this.cache = builder.build();
    }

    @Test
    void reactiveCache() {
        Mono<Long> valueMono = Mono.just(1L);

        cache.put(1, valueMono);

        Mono<Long> testMono1 = cache.getIfPresent(1);
        Mono<Long> testMono2 = cache.getIfPresent(2);

        Long test1 = testMono1.block();
        Long test2 = testMono2.block();

        assertEquals(test1, 1L);
        assertEquals(test2, null);
    }
}
