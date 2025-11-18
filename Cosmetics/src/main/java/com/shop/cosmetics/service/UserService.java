package com.shop.cosmetics.service;

import com.shop.cosmetics.dto.UpdateProfile;
import com.shop.cosmetics.dto.UserDto;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.enums.ErrorCode;
import com.shop.cosmetics.enums.Roles;
import com.shop.cosmetics.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shop.cosmetics.Utils.Helpers.getCurrentUser;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    ModelMapper modelMapper;

    public User getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user ;
    }

    public UserDto getProfile(){
        User user= getCurrentUser();
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto accessUserInformation(Long id){
        User user = getCurrentUser();
        if(!user.getRoles().contains(Roles.ADMIN)){
            throw new RuntimeException("You not allowed to access");
        }
        var customer = userRepository.findById(id);
        return modelMapper.map(customer, UserDto.class);
    }

    public UserDto updateProfile (UpdateProfile request){
        User user = getCurrentUser();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true);
        modelMapper.map(request, user);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
    }

    public List<UserDto> getAll(){
        User user = getCurrentUser();
        if (!user.getRoles().contains(Roles.ADMIN)) {
            throw new RuntimeException("You are not allowed to access");
        }

        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        return user;
    }



}
