package com.rim.auth.model;

import java.util.UUID;

import lombok.Data;

@Data
public class UserFilterModel {

    private String email;
    private String firstName;
    private String lastName;
    private UUID roleId;
    private Boolean active;

    private int page = 0;
    private int size = 10;
}
