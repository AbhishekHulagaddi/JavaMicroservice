package com.rim.auth.model;

import java.util.UUID;

import lombok.Data;

@Data
public class PermissionModel {

    private UUID permissionId;

    private String permissionName;

    private UUID roleId;
    
    private String roleName;
    
    private UUID resourceId;
    
    private String resourceName;
    
    private UUID scopeId;
    
    private String scopeName;
    
    private boolean permissionStatus;
    
}
