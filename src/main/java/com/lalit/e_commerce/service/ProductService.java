package com.lalit.e_commerce.service;

import com.lalit.e_commerce.dto.request.ProductFilterRequest;
import com.lalit.e_commerce.dto.request.ProductRequest;
import com.lalit.e_commerce.dto.response.PageResponse;
import com.lalit.e_commerce.dto.response.ProductResponse;
import com.lalit.e_commerce.entity.Product;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse  getProductById(Long id);

    PageResponse<ProductResponse > getAllProducts(
            int page,int size,String sortBy,String direction);

    ProductResponse  updateProduct(Long id,ProductRequest request);

    void deleteProduct(Long id);

    List<ProductResponse > getProductsByCategory(Long categoryId);

    PageResponse<ProductResponse> searchProducts(
            String keyword,int page, int size, String sortBy, String direction);


    PageResponse<ProductResponse> searchWithAdvancedFilters(
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Double minRating,
            Boolean inStock,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
