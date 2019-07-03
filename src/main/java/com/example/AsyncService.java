package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncService {

    private Map<String, String> greetings = new HashMap<String, String>() {
        {
            put("CH", "gruetzi");
            put("DE", "hallo");
            put("HU", "szia");
        }
    };

    // the caller returns immediately upon invocation
    // the execution of the method occurs in a task that has been submitted to a Spring TaskExecutor
    @Async
    public CompletableFuture<String> greet(String language) {
        log.info("thread: {}", Thread.currentThread().getName());
        delay(500);
        String greeting = greetings.get(language);
        if (greeting == null) {
            throw new IllegalArgumentException("do not speak " + language);
        }
        return CompletableFuture.completedFuture(greetings.get(language));
    }

    private void delay(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
