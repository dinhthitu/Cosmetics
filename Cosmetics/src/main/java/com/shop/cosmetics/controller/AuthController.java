package com.shop.cosmetics.controller;
import com.shop.cosmetics.dto.Login;
import com.shop.cosmetics.dto.Register;
import com.shop.cosmetics.dto.UserDto;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.enums.Roles;
import com.shop.cosmetics.repository.UserRepository;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.response.LoginResponse;
import com.shop.cosmetics.response.RegisterResponse;
import com.shop.cosmetics.service.AuthService;
//import com.shop.cosmetics.service.OauthService;
import com.shop.cosmetics.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;
    UserRepository userRepository;
//    JavaMailSender javaMailSender;
//    OauthService oauthService;
//    ModelMapper modelMapper;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody Register request){
        return ApiResponse.<RegisterResponse>builder()
                .result(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody Login request, HttpServletResponse response){
        LoginResponse tokens = authService.login(request);
        Cookie cookie = new Cookie("refreshToken", tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ApiResponse.<LoginResponse>builder()
                .result(LoginResponse.builder()
                        .accessToken(tokens.getAccessToken())
                        .build())
                .build();
    }


    @PostMapping("/refreshToken")
    public ApiResponse<String> refreshToken(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));
        return ApiResponse.<String>builder()
                .result(authService.refreshToken(refreshToken))
                .build();
    }


}
