package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.CategoryRequest;
import com.lalit.e_commerce.entity.Category;
import com.lalit.e_commerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Create new category",
            description = "Creates a new product category. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Category> createCategory(
           @Valid @RequestBody CategoryRequest request){
        Category category=categoryService.createCategory(request);

        return new ResponseEntity<>(category,HttpStatus.CREATED);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(
            @Parameter(description = "Category ID") @PathVariable Long id){

        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){

        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(
            summary = "Update category",
            description = "Updates a product category. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "Category ID") @PathVariable Long id,
           @Valid @RequestBody CategoryRequest request){

        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(
            summary = "Delete category",
            description = "Deletes a product category. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
           @Parameter(description = "Category ID") @PathVariable Long id){

        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
