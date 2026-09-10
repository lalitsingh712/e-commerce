package com.lalit.e_commerce.repository;

import com.lalit.e_commerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByActiveTrue();

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Product> findByNameContainingIgnoreCase(String name);


    //Advanced search with filters
    @Query("SELECT p FROM Product p " +
            "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR :keyword IS NULL) " +
            "AND (p.price BETWEEN :minPrice AND :maxPrice) " +
            "AND (p.category.id = :categoryId OR :categoryId IS NULL) " +
            "AND (p.averageRating >= :minRating OR :minRating IS NULL) " +
            "AND (p.stockQuantity > 0 OR :inStock = false) " +
            "AND p.active = true")
    Page<Product> searchWithAdvancedFilters(
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryId") Long categoryId,
            @Param("minRating") Double minRating,
            @Param("inStock") Boolean inStock,
            Pageable pageable
    );

}
