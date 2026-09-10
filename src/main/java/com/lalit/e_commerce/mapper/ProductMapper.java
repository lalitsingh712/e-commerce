package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.ProductResponse;
import com.lalit.e_commerce.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper
{

    public ProductResponse toResponse(Product product){

        if (product == null) {
            return null;
        }

        ProductResponse response=new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setSku(product.getSku());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.isActive());

        if(product.getCategory()!=null){
            response.setCategoryId(product.getCategory().getId());

            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }
}
