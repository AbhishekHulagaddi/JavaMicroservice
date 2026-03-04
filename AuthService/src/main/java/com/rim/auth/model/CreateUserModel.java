package com.rim.auth.model;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateUserModel {

    private UUID userId;
    private String email;
    private Long otp;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean active;
    private boolean emailVerified;
    private String roleName;
    private UUID roleId;
    private String password;
    private String confirmPassword;

}
