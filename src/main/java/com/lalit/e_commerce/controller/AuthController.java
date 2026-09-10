package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.LoginRequest;
import com.lalit.e_commerce.dto.response.LoginResponse;
import com.lalit.e_commerce.entity.User;
import com.lalit.e_commerce.exception.BadRequestException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.UserRepository;
import com.lalit.e_commerce.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication",description = "APIs for user authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "User login",
            description = "Authenticates user with email and password, returns JWT token for subsequent API calls."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, JWT token returned",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email or password"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid  @RequestBody LoginRequest request){
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + request.getEmail())
                );

        String token = jwtService.generateToken(
                user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(
                new LoginResponse(token, user.getEmail(), user.getRole().name()));
    }catch(BadCredentialsException e){
        throw new BadCredentialsException("Invalid email or Password");
    }
    }
}
