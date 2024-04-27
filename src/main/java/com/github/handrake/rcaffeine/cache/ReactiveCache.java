package com.github.handrake.rcaffeine.cache;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface ReactiveCache<K, V> {
    Mono<V> getIfPresent(K key);

    Mono<V> get(K key, Function<? super K, ? extends V> mappingFunction);

    Mono<V> getByMono(K key, Function<? super K, ? extends Mono<? extends V>> mappingFunction);

    Mono<Map<K, V>> getAll(Iterable<? extends K> keys,
                           Function<? super Set<? extends K>, ? extends Map<? extends K, ? extends V>> mappingFunction);

    void put(K key, Mono<? extends V> valueFunc);
}
