package com.lalit.e_commerce.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 150, message = "Name must be between 3 and 150 characters")
    private String name;

    @Size(max = 1000, message = "Description can not exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock Quantity is required")
    @Min(value = 0, message = "Stock can not be negative")
    private Integer stockQuantity;

    @NotNull(message = "SKU is required")
    @Size(min = 1, max = 50, message = "SKU must be between 1 and 50 characters")
    private String sku;

    @URL(message = "Image URL must be valid")
    private String imageUrl;

    @NotNull(message = "CategoryId is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

}
