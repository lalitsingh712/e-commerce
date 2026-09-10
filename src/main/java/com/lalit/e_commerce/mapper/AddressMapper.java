package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.AddressResponse;
import com.lalit.e_commerce.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse toResponse(Address address){

        AddressResponse response = new AddressResponse();

        response.setId(address.getId());
        response.setStreet(address.getAddressLine());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());
        response.setCountry(address.getCountry());
        response.setPhone(address.getUser().getPhone());

        return response;
    }
}
