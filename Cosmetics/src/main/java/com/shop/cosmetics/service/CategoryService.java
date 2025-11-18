package com.shop.cosmetics.service;

import com.shop.cosmetics.Utils.Helpers;
import com.shop.cosmetics.config.AppMapstruct;
import com.shop.cosmetics.dto.CategoryDto;
import com.shop.cosmetics.entity.Category;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.enums.ErrorCode;
import com.shop.cosmetics.exception.Exceptions;
import com.shop.cosmetics.repository.CategoryRepository;
import com.shop.cosmetics.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    AppMapstruct mapstructConfig;

    public CategoryDto createCategory(CategoryDto categoryDto){
       Category category = categoryRepository.findByCategoryName(categoryDto.getCategoryName());
       if(category !=null){
           throw new Exceptions(ErrorCode.PRODUCT_EXISTED);
       }
        User user = Helpers.getCurrentUser();
        Helpers.CheckUserPermission(user, category);
        category = mapstructConfig.toCategory(categoryDto);
        categoryRepository.save(category);
        return mapstructConfig.toCategoryDto(category);
    }

    public CategoryDto getCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exceptions(ErrorCode.CATEGORY_EXISTED));

        return mapstructConfig.toCategoryDto(category);
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exceptions(ErrorCode.CATEGORY_NOT_FOUND));

        User user = Helpers.getCurrentUser();
        Helpers.CheckUserPermission(user, category);
        mapstructConfig.updateCategory(category, categoryDto);
        return mapstructConfig.toCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exceptions(ErrorCode.CATEGORY_EXISTED));

        User user = Helpers.getCurrentUser();
        Helpers.CheckUserPermission(user, category);

        categoryRepository.deleteById(id);
    }

    public List<CategoryDto> getAll(){
        return categoryRepository.findAll().stream()
                .map(mapstructConfig::toCategoryDto)
                .toList();
    }

}
