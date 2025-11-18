package com.shop.cosmetics.entity;

import com.shop.cosmetics.annotation.PasswordValidation;
import com.shop.cosmetics.enums.Gender;
import com.shop.cosmetics.enums.Roles;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;
    @Column(nullable = false)
    String username;
    @PasswordValidation
    String password;
    String email;
    String googleId;
    String phoneNumber;
    String address;
    @OneToOne
    @JoinColumn(name = "cartId")
    Cart cart;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    Set<Roles> roles;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId, user.userId);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(("ROLE_"+role.name())))
                .collect(Collectors.toSet());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // hoặc custom logic nếu bạn muốn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
