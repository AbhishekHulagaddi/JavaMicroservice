package com.rim.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rim.auth.model.RoleModel;
import com.rim.auth.service.AuthRoleService;
import com.rim.auth.utils.WebConstantUrl;


@RestController
@RequestMapping(WebConstantUrl.ROLE)
public class AuthRoleController {

    @Autowired
    private AuthRoleService authRoleService;

    // ============================================
    // CREATE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.CREATE)
    public Map<String, Object> createRole(@RequestBody RoleModel roleModel) {
        return authRoleService.createRole(roleModel);
    }

    // ============================================
    // UPDATE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.UPDATE)
    public Map<String, Object> updateRole(@RequestBody RoleModel roleModel) {
        return authRoleService.updateRole(roleModel);
    }

    // ============================================
    // DELETE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.DELETE)
    public Map<String, Object> deleteRole(@RequestBody RoleModel roleModel) {
        return authRoleService.deleteRole(roleModel);
    }

    // ============================================
    // GET ROLE BY ID
    // ============================================
    @PostMapping(WebConstantUrl.GET_BY_ID)
    public Map<String, Object> getRoleById(@RequestBody RoleModel roleModel) {
        return authRoleService.getRoleByRoleId(roleModel);
    }

    // ============================================
    // GET ALL ROLES
    // ============================================
    @PostMapping(WebConstantUrl.GET_ALL)
    public Map<String, Object> getAllRoles() {
        return authRoleService.getAllRoles();
    }
}
