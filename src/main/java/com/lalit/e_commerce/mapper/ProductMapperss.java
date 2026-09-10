/*
package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.ProductResponse;
import com.lalit.e_commerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.crypto.spec.PSource;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id",target = "categoryId")
    @Mapping(source = "category.name",target = "categoryName")
    ProductResponse toResponse(Product product);

    @Mapping(target = "category.id",source = "categoryId")
    Product toEntity(ProductResponse response);
}
*/
