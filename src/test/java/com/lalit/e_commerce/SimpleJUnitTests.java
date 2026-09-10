package com.lalit.e_commerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("E-Commerce JUnit Tests")
public class SimpleJUnitTests {

    @Test
    @DisplayName("Test String Equality")
    void testStringEquality(){
        String actual="Hello";
        String expected ="Hello";
        assertEquals(expected,actual,"String should match");
    }

    @Test
    @DisplayName("Test String Contains")
    void testStringContains(){
        String text="Spring boot Testing";
        assertTrue(text.contains("Spring"),"Text should contain 'Spring'");
    }

    @Test
    @DisplayName("Test Addition")
    void testAddition() {
        int sum = 5 + 3;
        assertEquals(8, sum, "5 + 3 should equal 8");
    }

    @Test
    @DisplayName("Test Division by Zero")
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            int result = 10 / 0;
        }, "Division by zero should throw ArithmeticException");
    }

    // ==================== Collection Tests ====================

    @Test
    @DisplayName("Test List Operations")
    void testListOperations() {
        java.util.List<String> items = new java.util.ArrayList<>();
        items.add("Mouse");
        items.add("Keyboard");

        assertEquals(2, items.size(), "List should have 2 items");
        assertTrue(items.contains("Mouse"), "List should contain 'Mouse'");
    }

    // ==================== Object Tests ====================

    @Test
    @DisplayName("Test Object Not Null")
    void testObjectNotNull() {
        String text = "Hello";
        assertNotNull(text, "Text should not be null");
    }

    @Test
    @DisplayName("Test Object Is Null")
    void testObjectIsNull() {
        String text = null;
        assertNull(text, "Text should be null");
    }

    // ==================== Boolean Tests ====================

    @Test
    @DisplayName("Test Boolean True")
    void testBooleanTrue() {
        boolean isValid = true;
        assertTrue(isValid, "Value should be true");
    }

    @Test
    @DisplayName("Test Boolean False")
    void testBooleanFalse() {
        boolean isValid = false;
        assertFalse(isValid, "Value should be false");
    }

    // ==================== Multiple Assertions ====================

    @Test
    @DisplayName("Test Multiple Assertions")
    void testMultipleAssertions() {
        int number = 42;

        // All these must pass
        assertEquals(42, number);
        assertTrue(number > 0);
        assertFalse(number < 0);
        assertNotNull(number);
    }
}