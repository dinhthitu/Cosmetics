package com.shop.cosmetics.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {

    CATEGORY_EXISTED(302, "Category already existed", HttpStatus.FOUND),
    UNAUTHORIZED(401, "You are not allowed to access", HttpStatus.UNAUTHORIZED),

    PRODUCT_EXISTED(302, "Product 's already available", HttpStatus.FOUND),

    CATEGORY_NOT_FOUND(404,"Category not found", HttpStatus.NOT_FOUND ),

    PRODUCT_NOT_FOUND(404, "PRODUCT NOT FOUND", HttpStatus.NOT_FOUND);

    ;
    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
