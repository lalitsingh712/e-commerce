package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.ProductRequest;
import com.lalit.e_commerce.dto.response.PageResponse;
import com.lalit.e_commerce.dto.response.ProductResponse;
import com.lalit.e_commerce.entity.Product;
import com.lalit.e_commerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management",description = "APIs for managing products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//Create Product
    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with details. ADMIN access required."
    )
    @ApiResponses({
                @ApiResponse(
                        responseCode = "201",
                        description = "Product created successfully",
                        content = @Content(schema = @Schema(implementation = ProductResponse.class))
                ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body (validation error)"
            ),
            @ApiResponse(
                     responseCode = "401",
                    description = "Unauthorized - JWT token missing or invalid"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - ADMIN access required"
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){

        log.info("Creating new product: {}", request.getName());

        ProductResponse response=productService.createProduct(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Get product by id
    @Operation(
            summary = "Get product by ID",
            description = "Retrieves a single product by its ID. No authentication required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "Product ID") @PathVariable Long id){

        log.info("Fetching product with ID: {}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    //Get all products with pagination
    @Operation(
            summary = "Get all products",
            description = "Retrieves all products with pagination, sorting, and filtering. No authentication required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
           @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
           @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "10") int size,
           @Parameter(description = "Sort by field (id, name, price, stockQuantity)") @RequestParam(defaultValue = "id") String sortBy,
           @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String direction ){

        log.info("Fetching all products - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);

        return ResponseEntity.ok(productService.getAllProducts(page,size,sortBy,direction));
    }

    //Search products
    @Operation(
            summary = "Search products",
            description = "Search products by name with pagination and sorting. No authentication required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search results retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters"
            )
    })
    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
           @Parameter(description = "Search keyword") @RequestParam String keyword,
           @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
           @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
           @Parameter(description = "Sort by filter") @RequestParam(defaultValue = "id") String sortBy,
           @Parameter(description = "Sort by direction") @RequestParam(defaultValue = "asc") String direction) {

        log.info("Searching products with keyword: {}", keyword);

        return ResponseEntity.ok(productService.searchProducts(keyword,page,size,sortBy,direction));
    }

    //ADVANCED SEARCH
    @Operation(
            summary = "Advanced Search products",
            description = "Search products with multiple filters: keyword, price range, category, rating, stock status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search results retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters"
            )
    })
    @GetMapping("/search/advanced")
    public ResponseEntity<PageResponse<ProductResponse>> advancedSearch(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Minimum price") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Category ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Minimum rating (0-5)") @RequestParam(required = false) Double minRating,
            @Parameter(description = "Only in-stock products") @RequestParam(required = false) Boolean inStock,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Advanced search - keyword: {}, minPrice: {}, maxPrice: {}, categoryId: {}, minRating: {}, inStock: {}",
                keyword, minPrice, maxPrice, categoryId, minRating, inStock);

      /*  Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
        BigDecimal max = maxPrice != null ? maxPrice : new BigDecimal("999999");


        Page<Product> products = productRepo.findAll((root, query, cb) -> {
                    var predicates = new java.util.ArrayList<>();*/


        // Call service with all 10 parameters
        PageResponse<ProductResponse> response = productService.searchWithAdvancedFilters(
                keyword,
                minPrice,
                maxPrice,
                categoryId,
                minRating,
                inStock,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(response);
    }

    //Update Product
    @Operation(
            summary = "Update a product",
            description = "Updates an existing product. ADMIN access required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - ADMIN access required"
            )
    })
    @SecurityRequirement(name="bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody ProductRequest request){

        log.info("Updating product with ID: {}", id);

        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    //Delete product
    @Operation(
            summary = "Delete a product",
            description = "Deletes a product by ID. ADMIN access required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Product deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - ADMIN access required"
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id){

        log.info("Deleting product with ID: {}", id);

        productService.deleteProduct(id);
        return  ResponseEntity.noContent().build();
    }

    //Get product by Category ID
    @Operation(
            summary = "Get product by Category ID",
            description = "Retrieves a single product by its Category ID. No authentication required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @Parameter(description = "Product ID") @PathVariable Long categoryId){

        log.info("Fetching products for category: {}", categoryId);

        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }
}
