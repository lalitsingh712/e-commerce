package com.lalit.e_commerce.service;

import com.lalit.e_commerce.dto.request.CategoryRequest;
import com.lalit.e_commerce.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryRequest request);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long id,CategoryRequest request);

    void deleteCategory(Long id);
}
