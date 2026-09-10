package com.lalit.e_commerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {

    private String keyword;
    private BigDecimal minPrice;
    private  BigDecimal maxPrice;
    private Long categoryId;
    private double minRating;
    private Boolean inStock;
    private String sortBy;
    private String direction;
}
