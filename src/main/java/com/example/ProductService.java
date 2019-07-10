package com.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductServiceAsync productServiceAsync;

    @Cacheable(cacheNames = "total-price")
    public BigDecimal getTotalPrice(String... productIds) throws ProductServiceException {

        log.info("thread: {}", Thread.currentThread().getName());

        List<CompletableFuture<BigDecimal>> priceFutures = Arrays.stream(productIds)
                .map(productId -> productServiceAsync.getPrice(productId))
                .collect(toList());

        CompletableFuture<List<BigDecimal>> allCompleted = myAllOf(
                priceFutures.toArray(new CompletableFuture[priceFutures.size()]));

        CompletableFuture<BigDecimal> finalPriceFuture = allCompleted.thenApply(prices -> {
            log.info("all CompletableFutures completed successfully, thread: {}", Thread.currentThread().getName());
            return priceFutures.stream().map(completableFuture -> completableFuture.join())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        });


        // .handle, .exceptionally - to provide default value on exception
        // .whenComplete - stop the next chained .thenX() methods
        // .completeExceptionally
        try {
            return finalPriceFuture.join();
        } catch (CompletionException e) {
            throw new ProductServiceException(e.getCause().getMessage());
        }

    }

    public static CompletableFuture<List<BigDecimal>> myAllOf(CompletableFuture<?>... futures) {
        // Returns a new CompletableFuture that is completed when all of the given CompletableFutures complete
        // If any of the given CompletableFutures complete exceptionally, then the returned CompletableFuture also does so,
        // with a CompletionException holding this exception as its cause
        return CompletableFuture.allOf(futures)
                .thenApply(x -> Arrays.stream(futures)
                        .map(f -> (BigDecimal)f.join())
                        .collect(toList())
                );
    }

}
