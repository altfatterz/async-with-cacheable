package com.example.good;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheableService {

    private final AsyncService asyncService;

    @Cacheable(cacheNames = "greetings")
    public List<String> greet(String... languages) {
        log.info("thread: {}", Thread.currentThread().getName());

        List<CompletableFuture<String>> greetings = Arrays.stream(languages)
                .map(language -> asyncService.greet(language))
                .collect(Collectors.toList());

        // Returns a new CompletableFuture that is completed when all of the given CompletableFutures complete
        // If any of the given CompletableFutures complete exceptionally, then the returned CompletableFuture also does so,
        // with a CompletionException holding this exception as its cause
        CompletableFuture<Void> allCompletedWithoutResult = CompletableFuture.allOf(
                greetings.toArray(new CompletableFuture[greetings.size()]));

        // we need the results
        CompletableFuture<List<String>> completedWithResult = allCompletedWithoutResult
                .thenApply(a -> {
                    log.info("all CompletableFutures completed successfully, thread: {}", Thread.currentThread().getName());
                    return greetings.stream().map(completableFuture -> completableFuture.join())
                            .collect(Collectors.toList());
                });

        return completedWithResult.join();
    }

}
