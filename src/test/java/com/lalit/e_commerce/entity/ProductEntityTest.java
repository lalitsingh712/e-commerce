package com.lalit.e_commerce.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Entity Tests")
public class ProductEntityTest {

    private Product product;

    @BeforeEach
    void setUp(){
        product=new Product();
    }

    @Test
    @DisplayName("Should Create Product With Valid Data")
    void testCreateProductWithValidData() {
        product.setId(1L);
        product.setName("Wireless Mouse");
        product.setPrice(new BigDecimal("1299.00"));
        product.setSku("WM-001");

        assertEquals(1L,product.getId());
        assertEquals("Wireless Mouse",product.getName());
        assertEquals(0, product.getPrice().compareTo(new BigDecimal("1299.00")));
        assertEquals("WM-001", product.getSku());

    }

    @Test
    @DisplayName("Should Set and Get Product Name")
    void testSetAndGetProductName() {
        product.setName("Gaming Keyboard");
        assertEquals("Gaming Keyboard", product.getName());
    }

    @Test
    @DisplayName("Should Set and Get Product Price")
    void testSetAndGetProductPrice() {
        BigDecimal price = new BigDecimal("2499.00");
        product.setPrice(price);
        assertEquals(0, product.getPrice().compareTo(price));
    }

    @Test
    @DisplayName("Should Set and Get Stock Quantity")
    void testSetAndGetStockQuantity() {
        product.setStockQuantity(100);
        assertEquals(100, product.getStockQuantity());
    }

    @Test
    @DisplayName("Should Set Product as Active")
    void testSetProductActive() {
        product.setActive(true);
        assertTrue(product.isActive());
    }

    @Test
    @DisplayName("Should Set Product as Inactive")
    void testSetProductInactive() {
        product.setActive(false);
        assertFalse(product.isActive());
    }
}

