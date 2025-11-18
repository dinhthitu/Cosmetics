package com.shop.cosmetics.controller;

import com.shop.cosmetics.dto.UpdateProfile;
import com.shop.cosmetics.dto.UserDto;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id){
        return ApiResponse.<User>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<UserDto> getProfile(){
        return ApiResponse.<UserDto>builder()
                .result(userService.getProfile())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer/{id}")
    public ApiResponse<UserDto> accessUserInformation(@PathVariable Long id){
        return ApiResponse.<UserDto>builder()
                .result(userService.accessUserInformation(id))
                .build();
    }

    @PatchMapping("/update/{id}")
    public ApiResponse<UserDto> updateProfile(@RequestBody UpdateProfile request){
        return ApiResponse.<UserDto>builder()
                .result(userService.updateProfile(request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ApiResponse.<Void>builder().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ApiResponse<List<UserDto>> getAll(){
        return ApiResponse.<List<UserDto>>builder()
                .result(userService.getAll())
                .build();
    }

}
