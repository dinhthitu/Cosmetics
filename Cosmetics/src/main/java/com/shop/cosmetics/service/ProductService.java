package com.shop.cosmetics.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shop.cosmetics.Utils.Helpers;
import com.shop.cosmetics.config.AppMapstruct;
import com.shop.cosmetics.dto.ProductDto;
import com.shop.cosmetics.entity.Category;
import com.shop.cosmetics.entity.Product;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.enums.ErrorCode;
import com.shop.cosmetics.enums.Roles;
import com.shop.cosmetics.exception.Exceptions;
import com.shop.cosmetics.repository.CategoryRepository;
import com.shop.cosmetics.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.shop.cosmetics.Utils.Helpers.getCurrentUser;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    AppMapstruct mapstructConfig;
    CategoryRepository categoryRepository;
    Cloudinary cloudinary;

    public ProductDto createProduct(Long categoryId,ProductDto productDto){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exceptions(ErrorCode.CATEGORY_NOT_FOUND));

        User user = getCurrentUser();
        Helpers.CheckUserPermission(user, category);
        Product product = mapstructConfig.toProduct(productDto);
        product.setCategory(category);

        return mapstructConfig.toProductDto(productRepository.save(product));

    }

    public ProductDto updateProduct(Long id, Long categoryId, ProductDto productDto){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exceptions(ErrorCode.PRODUCT_NOT_FOUND));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exceptions(ErrorCode.CATEGORY_NOT_FOUND));

        User user = getCurrentUser();
        Helpers.CheckUserPermission(user, category);
        mapstructConfig.updateProduct(product, productDto);
        product.setCategory(category);
        return mapstructConfig.toProductDto(productRepository.save(product));
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exceptions(ErrorCode.PRODUCT_NOT_FOUND));

        Category category = product.getCategory();
        User user = getCurrentUser();
        Helpers.CheckUserPermission(user, category);
        productRepository.deleteById(id);

    }

    public ProductDto getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exceptions(ErrorCode.PRODUCT_NOT_FOUND));

        return mapstructConfig.toProductDto(product);
    }

    public List<ProductDto> getAll(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);
        if(pagedResult.hasContent()){
            return (List<ProductDto>) mapstructConfig.toProductDtoList(pagedResult.getContent());
        }else{
            return new ArrayList<>();
        }
    }
    @Transactional
    public List<String> uploadImages(Long productId, MultipartFile[] images) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exceptions(ErrorCode.PRODUCT_NOT_FOUND));

        User user = getCurrentUser();
        if (!user.getRoles().contains(Roles.ADMIN)) {
            throw new Exceptions(ErrorCode.UNAUTHORIZED);
        }

        if (images == null || images.length == 0) {
            throw new RuntimeException("No images uploaded");
        }

        for (MultipartFile file : images) {
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Invalid file format");
            }
        }

        List<String> existingImages = product.getImages();
        if (existingImages == null) {
            existingImages = new ArrayList<>();
        }

        for (MultipartFile file : images) {
            Map<String, Object> fileUpload = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "folder", "product",
                            "overwrite", true,
                            "public_id", UUID.randomUUID().toString()
                    )
            );
            existingImages.add(fileUpload.get("secure_url").toString());
        }

        product.setImages(existingImages);
        productRepository.save(product);

        return existingImages;
    }


    @Transactional
    public void deleteImage( Long productId, String image) throws Exception{
        User user = getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exceptions(ErrorCode.PRODUCT_NOT_FOUND));
        if(!user.getRoles().contains(Roles.ADMIN)){
            throw new Exceptions(ErrorCode.UNAUTHORIZED);
        }
        if(image == null || image.isEmpty()){
            return;
        }
        String[]parts = image.split("/");
        String fileName = parts[parts.length  - 1];
        String publicId = "product/"+fileName.substring(0, fileName.lastIndexOf("."));
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));

        if(product.getImages().size() > 0){
            List<String> remainingImages = new ArrayList<>(product.getImages());
            boolean removed = remainingImages.remove(image);
            if(removed){
                product.setImages(remainingImages);
                productRepository.save(product);
            }
        }
    }

}
