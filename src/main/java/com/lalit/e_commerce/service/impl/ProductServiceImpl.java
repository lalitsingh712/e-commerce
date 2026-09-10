package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.request.ProductRequest;
import com.lalit.e_commerce.dto.response.PageResponse;
import com.lalit.e_commerce.dto.response.ProductResponse;
import com.lalit.e_commerce.entity.Category;
import com.lalit.e_commerce.entity.Product;
import com.lalit.e_commerce.exception.BadRequestException;
import com.lalit.e_commerce.exception.DuplicateResourceException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.mapper.ProductMapper;
import com.lalit.e_commerce.repository.CategoryRepository;
import com.lalit.e_commerce.repository.ProductRepository;
import com.lalit.e_commerce.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper productMapper;

    private static final List<String> VALID_SORT_FIELDS =
            Arrays.asList("id", "name", "price", "stockQuantity", "createdDate");


    public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo, ProductMapper productMapper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        //check SKU
        if(productRepo.existsBySku(request.getSku())){
            throw new DuplicateResourceException(
                    "Product with sku already exists : "+request.getSku());
        }

        //find category
        Category category=categoryRepo.findById(request.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+request.getCategoryId()));

        //create product
        Product product=new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(request.getSku());
        product.setImageUrl(request.getImageUrl());
        product.setActive(true);

        Product saveProduct= productRepo.save(product);

        return productMapper.toResponse((saveProduct));
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product= productRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                "Product not found with id: " + id));

         return productMapper.toResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String direction) {

        validateSortField(sortBy); //add this
        validatePagination(page,size);

        Sort sort;

        if(direction.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }else{
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable= PageRequest.of(page,size,sort);

        Page<Product> productPage= productRepo.findAll(pageable);

        return convertToPageResponse(productPage);
    }

    private PageResponse<ProductResponse> convertToPageResponse(Page<Product> productPage){

        return new PageResponse<>(
                productPage.getContent()
                        .stream()
                        .map(productMapper::toResponse)
                        .toList(),

                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast()
        );
    }

    /*@Override
    public List<ProductResponse> getAllProducts() {

        return productRepo.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }*/

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id)
                );

        // Check SKU uniqueness (if changed)
        if (!product.getSku().equals(request.getSku()) &&
                productRepo.existsBySku(request.getSku())) {
            throw new DuplicateResourceException(
                    "Product with SKU already exists: " + request.getSku()
            );
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category not found with id: " + request.getCategoryId())
                    );
            product.setCategory(category);
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(request.getSku());
        product.setImageUrl(request.getImageUrl());

        Product updatedProduct = productRepo.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product=productRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id: "+id));

        productRepo.delete(product);

    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {

        if(!categoryRepo.existsById(categoryId)){
            throw new ResourceNotFoundException(
                    "Category not found with id: " + categoryId);
        }
        List<Product> products=productRepo.findByCategoryId(categoryId);
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    public PageResponse<ProductResponse> searchProducts(
            String keyword, int page, int size, String sortBy, String direction) {

        if(keyword==null || keyword.trim().isEmpty()){
            throw new BadRequestException("Search keyword cannot be empty");
        }
        validatePagination(page,size);
        validateSortField(sortBy);
        Sort sort;

        if(direction.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }else{
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable=PageRequest.of(page,size,sort);

        Page<Product> productPage=productRepo.findByNameContainingIgnoreCase(keyword,pageable);

        return convertToPageResponse(productPage);
    }



    private void validateSortField(String sortBy){
        //valid product field for sorting
        List<String> validFields= Arrays.asList("id", "name", "price", "stockQuantity", "createdDate");

        if(!validFields.contains(sortBy)){
            throw new BadRequestException(
                    "Invalid sort field: " + sortBy +
                            ". Valid fields are: " + String.join(", ", validFields)
            );
        }
    }

    private void validatePagination(int page, int size){

        if(page<0){
            throw new BadRequestException("Page number cannot be negative");
        }
        if(size<=0){
            throw new BadRequestException("Page size must be greater than 0");
        }
        if(size>100){
            throw new BadRequestException("Page size cannot exceed 100");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchWithAdvancedFilters(
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
    ) {
        log.info("Advanced search - keyword: {}, price: {} to {}, rating: {}",
                keyword, minPrice, maxPrice, minRating);

        // Step 1: Handle null values with defaults
        BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
        BigDecimal max = maxPrice != null ? maxPrice : new BigDecimal("999999");
        Double rating = minRating != null ? minRating : 0.0;
        Boolean stock = inStock != null ? inStock : false;

        // Step 2: Convert sortBy & direction to Sort object
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);

        // Step 3: Create Pageable (page, size, sort combined)
        Pageable pageable = PageRequest.of(page, size, sort);

        //USE SPECIFICATION/CRITERIA API
        Page<Product> productPage = productRepo.findAll((root, query, cb) -> {
                    var predicates = new java.util.ArrayList<Predicate>();

                    //keyword filter(search in names)
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        predicates.add(cb.like(cb.lower(root.get("name")),
                                "%" + keyword.toLowerCase() + "%"));
                    }

                    // Price range filter
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), min));
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), max));

                    // Category filter
                    if (categoryId != null) {
                        predicates.add(cb.equal(root.get("category").get("id"), categoryId));
                    }

                    // Rating filter
                    predicates.add(cb.greaterThanOrEqualTo(root.get("averageRating"), rating));

                    // Stock filter (if inStock=true, only show products with stock > 0)
                    if (stock) {
                        predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
                    }

                    // Active filter (only show active products)
                    predicates.add(cb.equal(root.get("active"), true));

                    // Combine all predicates with AND
                    return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
                },pageable);


       /*     // Step 4: Call repository with 7 parameters (6 search params + Pageable)
        Page<Product> products = productRepo.searchWithAdvancedFilters(
                keyword,      // 1: String
                min,          // 2: BigDecimal
                max,          // 3: BigDecimal
                categoryId,   // 4: Long
                rating,       // 5: Double
                stock,        // 6: Boolean
                pageable      // 7: Pageable (contains page, size, sort)
        );*/

        log.info("Found {} products total (showing {} on page {})",
                productPage.getTotalElements(),
                productPage.getContent().size(),
                page);

        //  Step 5: Convert to PageResponse
        return convertToPageResponse(productPage);
    }
}
