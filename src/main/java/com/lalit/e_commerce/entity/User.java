package com.lalit.e_commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role=Role.CUSTOMER;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> addresses=new ArrayList<>();

    @PrePersist
    public void onCreate(){
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();

        if(role == null){
            role = Role.CUSTOMER;
        }
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    public Role getRole() {
        return role;
    }
}
