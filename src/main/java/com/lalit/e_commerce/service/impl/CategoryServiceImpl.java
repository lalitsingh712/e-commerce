package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.request.CategoryRequest;
import com.lalit.e_commerce.entity.Category;
import com.lalit.e_commerce.exception.DuplicateResourceException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.CategoryRepository;
import com.lalit.e_commerce.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        if(categoryRepo.existsByName(request.getName())){
            throw new DuplicateResourceException("Category already exists.");
        }
        Category category=new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepo.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Category not found with id: "+id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest request) {
        Category category=getCategoryById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category=getCategoryById(id);
        categoryRepo.delete(category);
    }
}
