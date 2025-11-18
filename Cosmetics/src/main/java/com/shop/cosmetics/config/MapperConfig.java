package com.shop.cosmetics.config;

import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.response.RegisterResponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(User.class, RegisterResponse.class)
                .addMappings(m -> m.map(User::getUserId, RegisterResponse::setUserId));
        return mapper;
    }
}
