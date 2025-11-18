package com.shop.cosmetics.config;

import com.shop.cosmetics.dto.CategoryDto;
import com.shop.cosmetics.dto.ProductDto;
import com.shop.cosmetics.entity.Category;
import com.shop.cosmetics.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


import java.util.List;
@Mapper(componentModel = "spring",  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppMapstruct {
    Product toProduct(ProductDto productDto);

    ProductDto toProductDto(Product product);

    List<ProductDto> toProductDtoList(List<Product> products);
    Category toCategory(CategoryDto categoryDto);
    CategoryDto toCategoryDto(Category category);

    void updateProduct(@MappingTarget Product product, ProductDto productDto);
    void updateCategory(@MappingTarget Category category, CategoryDto categoryDto);
}
