package com.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductServiceAsync productServiceAsync;

    @Cacheable(cacheNames = "total-price")
    public BigDecimal getTotalPrice(String... productIds) {

        log.info("thread: {}", Thread.currentThread().getName());

        List<CompletableFuture<BigDecimal>> greetings = Arrays.stream(productIds)
                .map(language -> productServiceAsync.getPrice(language))
                .collect(Collectors.toList());

        // Returns a new CompletableFuture that is completed when all of the given CompletableFutures complete
        // If any of the given CompletableFutures complete exceptionally, then the returned CompletableFuture also does so,
        // with a CompletionException holding this exception as its cause
        CompletableFuture<Void> allCompletedWithoutResult = CompletableFuture.allOf(
                greetings.toArray(new CompletableFuture[greetings.size()]));

        // we need the results
        CompletableFuture<BigDecimal> completedWithResult = allCompletedWithoutResult
                .thenApply(a -> {
                    log.info("all CompletableFutures completed successfully, thread: {}", Thread.currentThread().getName());
                    return greetings.stream().map(completableFuture -> completableFuture.join())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                });

        return completedWithResult.join();

    }

}
