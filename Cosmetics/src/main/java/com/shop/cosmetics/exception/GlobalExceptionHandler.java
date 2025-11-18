package com.shop.cosmetics.exception;

import com.shop.cosmetics.enums.ErrorCode;
import com.shop.cosmetics.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exceptions.class)
    ResponseEntity<ApiResponse> handleExceptionClass(Exceptions e){
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = e.getErrorCode();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);

    }

    @ExceptionHandler( value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(404);
        apiResponse.setResult(e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleInvalidArgumentException(MethodArgumentNotValidException e){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(400);
        apiResponse.setResult(
                e.getBindingResult()
                        .getAllErrors()
                        .stream().map(error -> error.getDefaultMessage())
                        .toList()
        );
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
