package com.rim.auth.service;

import java.util.Map;
import com.rim.auth.model.PermissionModel;

public interface PermissionService {

    Map<String, Object> createPermission(PermissionModel model);

    Map<String, Object> updatePermission(PermissionModel model);

    Map<String, Object> deletePermission(PermissionModel model);

    Map<String, Object> getById(PermissionModel model);

    Map<String, Object> getAll();

    Map<String, Object> getByRoleId(PermissionModel model);
    
    Map<String, Object> checkPermission(PermissionModel model);
}
