package com.lalit.e_commerce.service;

import com.lalit.e_commerce.dto.request.UserRequest;
import com.lalit.e_commerce.dto.response.UserResponse;
import com.lalit.e_commerce.entity.User;

import java.util.List;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();
}
