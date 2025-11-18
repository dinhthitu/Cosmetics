package com.shop.cosmetics.controller;

import com.shop.cosmetics.dto.CategoryDto;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
public class CategoryController {

    CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ApiResponse<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        return ApiResponse.<CategoryDto>builder()
                .result(categoryService.createCategory(categoryDto))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDto> getById(@PathVariable Long id){
        return ApiResponse.<CategoryDto>builder()
                .result(categoryService.getCategoryById(id))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ApiResponse<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto){
        return ApiResponse.<CategoryDto>builder()
                .result(categoryService.updateCategory(id, categoryDto))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryDto>> getAll(){
        return ApiResponse.<List<CategoryDto>>builder()
                .result(categoryService.getAll())
                .build();
    }
}
