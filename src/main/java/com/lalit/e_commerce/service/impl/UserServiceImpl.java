package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.request.UserRequest;
import com.lalit.e_commerce.dto.response.UserResponse;
import com.lalit.e_commerce.entity.Role;
import com.lalit.e_commerce.entity.User;
import com.lalit.e_commerce.exception.DuplicateResourceException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.UserRepository;
import com.lalit.e_commerce.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public UserResponse registerUser(UserRequest request) {

        if(userRepo.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("User already exists with email: "+request.getEmail());
        }

        User user=new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.CUSTOMER);

        User saveUser=userRepo.save(user);
        return convertToResponse(saveUser);
    }

    private UserResponse convertToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().name());

        return response;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user= userRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ id));
        return convertToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUser=userRepo.findAll();
        return convertToResponse(allUser);
    }

    private List<UserResponse> convertToResponse(List<User> users) {
        return users.stream()
                .map(this::convertToResponse)
                .toList();
    }
}
