package com.rim.auth.model;

import java.util.UUID;

import lombok.Data;

@Data
public class RoleModel {

    private String roleName;
    private UUID roleId;
}
