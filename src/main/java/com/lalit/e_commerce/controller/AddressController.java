package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.AddressRequest;
import com.lalit.e_commerce.entity.Address;
import com.lalit.e_commerce.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Address Management", description = "APIs for managing user addresses")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(
            summary = "Create user address",
            description = "Creates a new address for a user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<Address> createAddress(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestBody AddressRequest request){

        Address address=addressService.createAddress(userId,request);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @Operation(summary = "Get user addresses")
    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<List<Address>> getUserAddresses(
            @Parameter(description = "User ID") @PathVariable Long userId){

        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @Operation(summary = "Get address by ID")
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<Address> getAddress(
           @Parameter(description = "Address ID") @PathVariable Long addressId){

        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    @Operation(summary = "Update address")
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(
            @Parameter(description = "Address ID") @PathVariable Long addressId,@RequestBody AddressRequest request){

        return ResponseEntity.ok(addressService.updateAddress(addressId,request));
    }

    @Operation(summary = "Delete address")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
           @Parameter(description = "Address ID") @PathVariable Long addressId){

        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
