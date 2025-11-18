package com.shop.cosmetics.controller;

import com.shop.cosmetics.dto.ProductDto;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new/{categoryId}")
    public ApiResponse<ProductDto> createProduct(@PathVariable Long categoryId, @RequestBody ProductDto productDto){
        return ApiResponse.<ProductDto>builder()
                .result(productService.createProduct(categoryId,productDto))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{categoryId}")
    public ApiResponse<ProductDto> updateProduct(@PathVariable Long categoryId, @RequestParam Long id, @RequestBody ProductDto productDto){
        return ApiResponse.<ProductDto>builder()
                .result(productService.updateProduct(categoryId, id, productDto))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDto> getById(@PathVariable Long id){
        return ApiResponse.<ProductDto>builder()
                .result(productService.getProductById(id))
                .build();
    }

    @GetMapping("/")
    public ApiResponse<List<ProductDto>> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "productId") String sortBy){
        return ApiResponse.<List<ProductDto>>builder()
                .result(productService.getAll(pageNo, pageSize, sortBy))
                .build();
    }

    @PostMapping(value = "/upload/image/{productId}", consumes = "multipart/form-data" )
    public ApiResponse<List<String>> uploadImages (@PathVariable Long productId,
                                                   @RequestParam MultipartFile[] files) throws Exception{
        return ApiResponse.<List<String>>builder()
                .result(productService.uploadImages(productId, files))
                .build();
    }

    @DeleteMapping("/delete/image/{productId}")
    public ApiResponse<Void> deleteImage ( @PathVariable Long productId,
                                           @RequestParam("fileUrl") String fileUrl) throws Exception {

        productService.deleteImage(productId, fileUrl);
        return ApiResponse.<Void>builder().build();
    }
}
