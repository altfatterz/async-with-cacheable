package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncWithCacheableApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testHappyFlow() {
        BigDecimal totalPrice = productService.getTotalPrice("1", "2", "3");
        System.out.printf("total price: %.2f\n", totalPrice);
        totalPrice = productService.getTotalPrice("1", "2", "3");
        System.out.printf("total price: %.2f\n", totalPrice);
    }

    @Test
    public void testWithException() {
        productService.getTotalPrice("1", "5", "3");
    }
}
