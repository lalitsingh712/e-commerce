package com.lalit.e_commerce.service;

import com.lalit.e_commerce.dto.request.AddressRequest;
import com.lalit.e_commerce.entity.Address;

import java.util.List;

public interface AddressService {

    Address createAddress(Long userId, AddressRequest request);

    List<Address> getUserAddresses(Long userId);

    Address getAddressById(Long addressId);

    Address updateAddress(Long addressId,AddressRequest request);

    void deleteAddress(Long addressId);
}
