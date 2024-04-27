package com.github.handrake.rcaffeine.cache;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.Executor;

public class ReactiveCacheBuilder<K, V> {
    private final Caffeine<K, V> underlying;


    public ReactiveCacheBuilder(Caffeine<K, V> underlying) {
        this.underlying = underlying;
    }

    public ReactiveCacheBuilder<K, V> initialCapacity(int initialCapacity) {
        underlying.initialCapacity(initialCapacity);

        return this;
    }

    public ReactiveCacheBuilder<K, V> executor(Executor executor) {
        underlying.executor(executor);

        return this;
    }

    public ReactiveCacheBuilder<K, V> maximumSize(long maximumSize) {
        underlying.maximumSize(maximumSize);

        return this;
    }

    public ReactiveCache<K, V> build() {
        return new ReactiveCacheImpl(this.underlying.buildAsync());
    }
}
