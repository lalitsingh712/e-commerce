package com.lalit.e_commerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false,unique = true)
    private String sku;

    private String imageUrl;

    private boolean active=true;

    @Column(columnDefinition = "DECIMAL(3,2) DEFAULT 0")
    public Double averageRating=0.0;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalReviews = 0;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="category_id",nullable = false)
    private Category category;

    @PrePersist
    public void onCreate(){
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

}
