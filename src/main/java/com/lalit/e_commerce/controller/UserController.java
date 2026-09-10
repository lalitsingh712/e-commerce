package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.UserRequest;
import com.lalit.e_commerce.dto.response.UserResponse;
import com.lalit.e_commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user registration and profile management")
public class UserController {

    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register new user",
            description = "Creates a new user account"
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRequest request){

            UserResponse response=userService.registerUser(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

     @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all users", description = "Retrieves all users. ADMIN access required.")
    public ResponseEntity<List<UserResponse>> getAllUsers(){

        List<UserResponse> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by id")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id){

        UserResponse user=userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
