package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTests {

    // under test
    private ProductService productService;

    @Mock
    private ProductServiceAsync productServiceAsync;

    @Before
    public void setUp() {
        productService = new ProductService(productServiceAsync);
    }

    @Test
    public void getTotalPrice() {
        when(productServiceAsync.getPrice("100")).thenReturn(completedFuture(BigDecimal.ONE));
        when(productServiceAsync.getPrice("200")).thenReturn(completedFuture(BigDecimal.TEN));

        assertThat(productService.getTotalPrice("100", "200")).isEqualTo(new BigDecimal(11));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTotalPriceWithException() {
        when(productServiceAsync.getPrice("50")).thenThrow(new IllegalArgumentException());
        productService.getTotalPrice("50");
    }

}
