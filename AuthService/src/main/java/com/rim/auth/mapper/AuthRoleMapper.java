package com.rim.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.rim.auth.domain.Role;
import com.rim.auth.model.RoleModel;

@Component
public class AuthRoleMapper {
	

    public RoleModel toModel(Role role) {
        if (role == null) return null;
        RoleModel model = new RoleModel();
        model.setRoleId(role.getRoleId());
        model.setRoleName(role.getRoleName());
        return model;
    }


    public Role toDomain(RoleModel model) {
        if (model == null) return null;
        Role role = new Role();
        role.setRoleId(model.getRoleId()); 
        role.setRoleName(model.getRoleName());
        return role;
    }

    public static void updateDomain(RoleModel model, Role role) {
        if (model == null || role == null) return;

        role.setRoleName(model.getRoleName());
        role.setUpdatedAt(LocalDateTime.now());
    }
}
