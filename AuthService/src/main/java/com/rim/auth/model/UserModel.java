package com.rim.auth.model;

import java.util.UUID;

import lombok.Data;

@Data
public class UserModel {
	
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean active;
    private boolean emailVerified;
    private String roleName;
    private UUID roleId;

}
