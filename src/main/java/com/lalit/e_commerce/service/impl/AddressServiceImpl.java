package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.request.AddressRequest;
import com.lalit.e_commerce.entity.Address;
import com.lalit.e_commerce.entity.User;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.AddressRepository;
import com.lalit.e_commerce.repository.UserRepository;
import com.lalit.e_commerce.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepo;
    private final UserRepository userRepo;

    public AddressServiceImpl(AddressRepository addressRepo, UserRepository userRepo) {
        this.addressRepo = addressRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Address createAddress(Long userId, AddressRequest request) {

        User user=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException(
                "User not found with Id "+ userId));
        Address address=new Address();
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setState(request.getState());
        address.setAddressType(request.getAddressType());

        address.setUser(user);
        return addressRepo.save(address);
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        if(!userRepo.existsById(userId)){
            throw new ResourceNotFoundException(
                    "User not found with id: "+userId);
        }
        return addressRepo.findByUserId(userId);
    }

    @Override
    public Address getAddressById(Long addressId) {
        return addressRepo.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address not found with id : "+addressId));
    }

    @Override
    public Address updateAddress(Long addressId, AddressRequest request) {

        Address address=addressRepo.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address not found with id : "+addressId));
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setAddressType(request.getAddressType());
        return addressRepo.save(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address address=addressRepo.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address not found with id: "+addressId));
        addressRepo.delete(address);
    }
}
