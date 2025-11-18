package com.shop.cosmetics.exception;

import com.shop.cosmetics.enums.ErrorCode;
import lombok.Data;

@Data
public class Exceptions extends RuntimeException{
    private ErrorCode errorCode;
    public Exceptions(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
