package com.example;

import com.example.good.AsyncService;
import com.example.good.CacheableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.Cache;

import java.util.concurrent.Executor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncWithCacheableApplicationTests {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private CacheableService cacheableService;

    @Autowired
    private Executor executor;

    @Test
    public void test() {
        cacheableService.greet("CH", "DE", "HU");
        cacheableService.greet("CH", "DE", "HU");
    }

    @Test
    public void testWithException() {
        cacheableService.greet("CH", "FI", "HU");
        cacheableService.greet("CH", "FI", "HU");
    }
}
