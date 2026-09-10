package com.lalit.e_commerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressLine;

    private String city;

    private String state;

    private String pincode;

    private String country;

    private String addressType;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;


}
