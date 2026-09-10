package com.lalit.e_commerce.repository;

import com.lalit.e_commerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findByUserId(Long userId);
}
