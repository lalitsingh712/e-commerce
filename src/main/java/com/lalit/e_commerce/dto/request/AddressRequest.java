package com.lalit.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank(message = "Street is required")
    @Size(min = 5, max = 100, message = "Street must be between 5 and 100 characters")
    private String addressLine;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    private String state;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[0-9]{5,6}$", message = "Postal code must be 5-6 digits")
    private String pincode;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    private String country;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Phone is required")
    private String addressType;


}
