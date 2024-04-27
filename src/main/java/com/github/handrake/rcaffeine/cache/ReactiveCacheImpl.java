package com.github.handrake.rcaffeine.cache;

import com.github.benmanes.caffeine.cache.AsyncCache;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

class ReactiveCacheImpl<K, V> implements ReactiveCache<K, V> {
    private AsyncCache<K, V> underlying;


    public ReactiveCacheImpl(AsyncCache<K, V> underlying) {
        this.underlying = underlying;
    }

    public AsyncCache<K, V> getUnderlying() {
        return underlying;
    }

    @Override
    public Mono<V> getIfPresent(K key) {
        CompletableFuture<V> valueFuture = underlying.getIfPresent(key);

        if (valueFuture != null) {
            return Mono.fromFuture(valueFuture);
        } else {
            return Mono.empty();
        }
    }

    @Override
    public Mono<V> get(K key, Function<? super K, ? extends V> mappingFunction) {
        requireNonNull(mappingFunction);

        return Mono.fromFuture(underlying.get(key, mappingFunction));
    }

    @Override
    public Mono<V> getByMono(K key, Function<? super K, ? extends Mono<? extends V>> mappingFunction) {
        requireNonNull(mappingFunction);

        return Mono.fromFuture(underlying.get(key, (K k, Executor _executor) ->
                        mappingFunction
                                .apply(k)
                                .toFuture()
                ));
    }

    @Override
    public Mono<Map<K, V>> getAll(Iterable<? extends K> keys, Function<? super Set<? extends K>, ? extends Map<? extends K, ? extends V>> mappingFunction) {
        return Mono.fromFuture(underlying.getAll(keys, mappingFunction));
    }

    @Override
    public void put(K key, Mono<? extends V> valueFunc) {
        underlying.put(key, valueFunc.toFuture());
    }
}
