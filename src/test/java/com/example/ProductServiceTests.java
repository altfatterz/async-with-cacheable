package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

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
    public void getTotalPrice() throws ProductServiceException {
        when(productServiceAsync.getPrice("100")).thenReturn(completedFuture(BigDecimal.ONE));
        when(productServiceAsync.getPrice("200")).thenReturn(completedFuture(BigDecimal.TEN));

        assertThat(productService.getTotalPrice("100", "200")).isEqualTo(new BigDecimal(11));
    }

    @Test(expected = ProductServiceException.class)
    public void getTotalPriceWithException() throws ProductServiceException {

        CompletableFuture<BigDecimal> result = new CompletableFuture<>();
        result.completeExceptionally(new IllegalArgumentException("not found"));

        when(productServiceAsync.getPrice("50")).thenReturn(result);
        productService.getTotalPrice("50");
    }

}
