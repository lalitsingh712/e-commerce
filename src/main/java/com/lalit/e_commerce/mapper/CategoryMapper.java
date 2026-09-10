package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.CategoryResponse;
import com.lalit.e_commerce.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category){

        CategoryResponse response=new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return response;
    }
}
