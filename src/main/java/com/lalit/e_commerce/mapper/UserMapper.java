package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.UserResponse;
import com.lalit.e_commerce.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole() != null ? user.getRole().toString() : "USER");

        return response;
    }
}
