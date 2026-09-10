package com.lalit.e_commerce.dto.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("ProductResponse DTO Tests")
class ProductResponseTest {

    private ProductResponse response;

    @BeforeEach
    void setUp() {
        response = new ProductResponse();
    }

    @Test
    @DisplayName("Should Create ProductResponse DTO")
    void testCreateProductResponse() {
        response.setId(1L);
        response.setName("Wireless Mouse");
        response.setPrice(new BigDecimal("1299.00"));
        response.setSku("WM-001");
        response.setCategoryId(1L);
        response.setCategoryName("Electronics");

        assertEquals(1L, response.getId());
        assertEquals("Wireless Mouse", response.getName());
        assertEquals(new BigDecimal("1299.00"), response.getPrice());
        assertEquals("WM-001", response.getSku());
        assertEquals(1L, response.getCategoryId());
        assertEquals("Electronics", response.getCategoryName());
    }

    @Test
    @DisplayName("Should Set All Fields")
    void testSetAllFields() {
        response.setId(2L);
        response.setName("Gaming Keyboard");
        response.setDescription("Mechanical RGB Keyboard");
        response.setPrice(new BigDecimal("2499.00"));
        response.setStockQuantity(50);
        response.setSku("GK-001");
        response.setImageUrl("https://example.com/keyboard.jpg");

        assertNotNull(response.getId());
        assertNotNull(response.getName());
        assertNotNull(response.getDescription());
        assertNotNull(response.getPrice());
        assertTrue(response.getStockQuantity() > 0);
    }

    @Test
    @DisplayName("Should Get All Fields")
    void testGetAllFields() {
        response.setId(3L);
        response.setName("USB Mouse");

        assertEquals(3L, response.getId());
        assertEquals("USB Mouse", response.getName());
    }
}