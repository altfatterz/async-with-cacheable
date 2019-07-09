package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductServiceAsync {

    private Map<String, BigDecimal> prices = new HashMap<String, BigDecimal>() {
        {
            put("1", new BigDecimal(9.15));
            put("2", new BigDecimal(52.95));
            put("3", new BigDecimal(15.20));
        }
    };


    // the caller returns immediately upon invocation
    // the execution of the method occurs in a task that has been submitted to a Spring TaskExecutor
    @Async
    CompletableFuture<BigDecimal> getPrice(String productId) {
        log.info("thread: {}", Thread.currentThread().getName());
        delay(2000);
        BigDecimal price = prices.get(productId);
        if (price == null) {
            throw new IllegalArgumentException("not found: " + productId);
        }
        return CompletableFuture.completedFuture(price);
    }

    private void delay(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
