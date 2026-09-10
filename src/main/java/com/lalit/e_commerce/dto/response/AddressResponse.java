package com.lalit.e_commerce.dto.response;

import lombok.Data;

@Data
public class AddressResponse {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private String phone;
}
