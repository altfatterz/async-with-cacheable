package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncWithCacheableApplicationTests {

    @Autowired
    private CacheableService cacheableService;

    @Test
    public void testHappyFlow() {
        cacheableService.greet("CH", "DE", "HU");
        cacheableService.greet("CH", "DE", "HU");
    }

    @Test
    public void testWithException() {
        cacheableService.greet("CH", "FI", "HU");
        //cacheableService.greet("CH", "FI", "HU");
    }
}
