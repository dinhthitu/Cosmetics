package com.shop.cosmetics.service;

import com.shop.cosmetics.dto.Login;
import com.shop.cosmetics.dto.Register;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.enums.Roles;
import com.shop.cosmetics.repository.UserRepository;
import com.shop.cosmetics.response.LoginResponse;
import com.shop.cosmetics.response.RegisterResponse;
import com.shop.cosmetics.security.JWTService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {


    UserRepository userRepository;
    JWTService jwtService;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    CartService cartService;
    AuthenticationManager authenticationManager;
    EmailService emailService;

    public RegisterResponse register(Register request) {
        var user = userRepository.findByEmail(request.getEmail());
        if(user != null){
            throw new RuntimeException("User existed"); }

        user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Nếu chưa có user nào => đây là user đầu tiên => ADMIN
        if (userRepository.count() == 0) {
            user.setRoles(Set.of(Roles.ADMIN));
        } else {
            user.setRoles(Set.of(Roles.CUSTOMER));
        }
        userRepository.save(user);
        cartService.createCart(user);
        emailService.sendWelcomeMessage(request.getEmail(), request.getUsername());
        return modelMapper.map(user, RegisterResponse.class);
    }


    public LoginResponse login (Login request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = (User)authentication.getPrincipal();
        String accessToken = jwtService.accessToken(user);
        String refreshToken = jwtService.refreshToken(user);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }


    public String refreshToken(String token){
        Long id = jwtService.getUserIdFromToken(token);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
        return jwtService.accessToken(user);
    }
}
