package com.shop.cosmetics.Utils;

import com.shop.cosmetics.entity.Cart;
import com.shop.cosmetics.entity.Category;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.enums.ErrorCode;
import com.shop.cosmetics.enums.Roles;
import com.shop.cosmetics.exception.Exceptions;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class Helpers {
    public static User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new Exceptions(ErrorCode.UNAUTHORIZED);
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof User user)) {
            throw new Exceptions(ErrorCode.UNAUTHORIZED);
        }

        return user;
    }

    public static void CheckUserPermission(User user, Category category){
        if(!user.getRoles().contains(Roles.ADMIN)){
            throw new Exceptions(ErrorCode.UNAUTHORIZED);
        }
    }

    public static void CheckCartPermission(User user, Cart cart){
        if(!cart.getUser().getUserId().equals(user.getUserId())){
            throw new Exceptions((ErrorCode.UNAUTHORIZED));
        }
    }
}
