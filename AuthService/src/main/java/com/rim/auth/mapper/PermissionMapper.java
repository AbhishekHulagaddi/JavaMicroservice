package com.rim.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.rim.auth.domain.Permission;
import com.rim.auth.domain.Resource;
import com.rim.auth.domain.Role;
import com.rim.auth.domain.Scope;
import com.rim.auth.model.PermissionModel;

@Component
public class PermissionMapper {

    // =====================================================
    // DOMAIN → MODEL
    // =====================================================
    public PermissionModel toModel(Permission permission) {
        if (permission == null) return null;

        PermissionModel model = new PermissionModel();

        model.setPermissionId(permission.getPermissionId());
        model.setPermissionName(permission.getPermissionName());
        model.setPermissionStatus(permission.isPermissionStatus());

        // ROLE
        if (permission.getRole() != null) {
            model.setRoleId(permission.getRole().getRoleId());
            model.setRoleName(permission.getRole().getRoleName());
        }

        // RESOURCE
        if (permission.getResource() != null) {
            model.setResourceId(permission.getResource().getResourceId());
            model.setResourceName(permission.getResource().getResourceName());
        }

        // SCOPE
        if (permission.getScope() != null) {
            model.setScopeId(permission.getScope().getScopeId());
            model.setScopeName(permission.getScope().getScopeName());
        }

        return model;
    }

    // =====================================================
    // DTO → DOMAIN (CREATE)
    // =====================================================
    public Permission toDomain(
            PermissionModel model,
            Role role,
            Resource resource,
            Scope scope
    ) {
        if (model == null) return null;

        Permission permission = new Permission();

        permission.setPermissionName(model.getPermissionName());
        permission.setPermissionStatus(model.isPermissionStatus());
        permission.setActive(true);
        permission.setCreatedAt(LocalDateTime.now());

        permission.setRole(role);
        permission.setResource(resource);
        permission.setScope(scope);

        return permission;
    }

    // =====================================================
    // UPDATE DOMAIN FROM DTO
    // =====================================================
    public void updateDomain(
            PermissionModel model,
            Permission permission,
            Role role,
            Resource resource,
            Scope scope
    ) {
        if (model == null || permission == null) return;

        if (model.getPermissionName() != null)
            permission.setPermissionName(model.getPermissionName());

        if (role != null)
            permission.setRole(role);

        if (resource != null)
            permission.setResource(resource);

        if (scope != null)
            permission.setScope(scope);

        permission.setPermissionStatus(model.isPermissionStatus());
        permission.setUpdatedAt(LocalDateTime.now());
    }
}
