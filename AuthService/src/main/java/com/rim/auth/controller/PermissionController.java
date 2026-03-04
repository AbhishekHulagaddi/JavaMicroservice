package com.rim.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rim.auth.model.PermissionModel;
import com.rim.auth.service.PermissionService;
import com.rim.auth.utils.WebConstantUrl;

import java.util.Map;

@RestController
@RequestMapping(WebConstantUrl.PERMISSION)
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // =====================================================
    // CREATE
    // =====================================================
    @PostMapping(WebConstantUrl.CREATE)
    public Map<String, Object> create(@RequestBody PermissionModel model) {
        return permissionService.createPermission(model);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @PostMapping(WebConstantUrl.UPDATE)
    public Map<String, Object> update(@RequestBody PermissionModel model) {
        return permissionService.updatePermission(model);
    }

    // =====================================================
    // DELETE (SOFT)
    // =====================================================
    @PostMapping(WebConstantUrl.DELETE)
    public Map<String, Object> delete(@RequestBody PermissionModel model) {
        return permissionService.deletePermission(model);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @PostMapping(WebConstantUrl.GET_BY_ID)
    public Map<String, Object> getById(@RequestBody PermissionModel model) {
        return permissionService.getById(model);
    }

    // =====================================================
    // GET ALL
    // =====================================================
    @PostMapping(WebConstantUrl.GET_ALL)
    public Map<String, Object> getAll() {
        return permissionService.getAll();
    }

    // =====================================================
    // GET BY ROLE
    // =====================================================
    @PostMapping(WebConstantUrl.GET_BY_ROLEID)
    public Map<String, Object> getByRole(@RequestBody PermissionModel model) {
        return permissionService.getByRoleId(model);
    }
    // =====================================================
    // CheckPermission
    // =====================================================
    @PostMapping(WebConstantUrl.CHECK)
    public Map<String, Object> checkPermission(@RequestBody PermissionModel model) {
        return permissionService.checkPermission(model);
    }
}
