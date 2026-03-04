package com.rim.auth.serviceimpl;


import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rim.auth.domain.Permission;
import com.rim.auth.domain.Resource;
import com.rim.auth.domain.Role;
import com.rim.auth.domain.Scope;
import com.rim.auth.mapper.PermissionMapper;
import com.rim.auth.model.PermissionModel;
import com.rim.auth.repo.AuthRoleRepo;
import com.rim.auth.repo.PermissionRepo;
import com.rim.auth.repo.ResourceRepo;
import com.rim.auth.repo.ScopeRepo;
import com.rim.auth.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private AuthRoleRepo roleRepo;

    @Autowired
    private ResourceRepo resourceRepo;

    @Autowired
    private ScopeRepo scopeRepo;

    @Autowired
    private PermissionMapper permissionMapper;

    // common response
    private Map<String, Object> response(boolean status, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", data);
        return map;
    }

    // =====================================================
    // CREATE
    // =====================================================
    @Override
    public Map<String, Object> createPermission(PermissionModel model) {

        if (model == null)
            return response(false, "Invalid request", null);

        if (model.getRoleId() == null || model.getResourceId() == null || model.getScopeId() == null)
            return response(false, "RoleId, ResourceId, ScopeId required", null);

        Role role = roleRepo.findById(model.getRoleId()).orElse(null);
        if (role == null) return response(false, "Role not found", null);

        Resource resource = resourceRepo.findById(model.getResourceId()).orElse(null);
        if (resource == null) return response(false, "Resource not found", null);

        Scope scope = scopeRepo.findById(model.getScopeId()).orElse(null);
        if (scope == null) return response(false, "Scope not found", null);

        Permission permission = permissionMapper.toDomain(model, role, resource, scope);
        permissionRepo.save(permission);

        return response(true, "Permission created", permissionMapper.toModel(permission));
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @Override
    public Map<String, Object> updatePermission(PermissionModel model) {

        if (model == null || model.getPermissionId() == null)
            return response(false, "PermissionId required", null);

        Optional<Permission> opt = permissionRepo.findById(model.getPermissionId());
        if (opt.isEmpty())
            return response(false, "Permission not found", null);

        Permission permission = opt.get();

        Role role = null;
        Resource resource = null;
        Scope scope = null;

        if (model.getRoleId() != null)
            role = roleRepo.findById(model.getRoleId()).orElse(null);

        if (model.getResourceId() != null)
            resource = resourceRepo.findById(model.getResourceId()).orElse(null);

        if (model.getScopeId() != null)
            scope = scopeRepo.findById(model.getScopeId()).orElse(null);

        permissionMapper.updateDomain(model, permission, role, resource, scope);

        permissionRepo.save(permission);

        return response(true, "Permission updated", permissionMapper.toModel(permission));
    }

    // =====================================================
    // DELETE (SOFT)
    // =====================================================
    @Override
    public Map<String, Object> deletePermission(PermissionModel model) {

        if (model == null || model.getPermissionId() == null)
            return response(false, "PermissionId required", null);

        Optional<Permission> opt = permissionRepo.findById(model.getPermissionId());
        if (opt.isEmpty())
            return response(false, "Permission not found", null);

        Permission permission = opt.get();
        permission.setActive(false);
        permission.setUpdatedAt(LocalDateTime.now());

        permissionRepo.save(permission);

        return response(true, "Permission deleted", null);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @Override
    public Map<String, Object> getById(PermissionModel model) {

        if (model == null || model.getPermissionId() == null)
            return response(false, "PermissionId required", null);

        Optional<Permission> opt = permissionRepo.findById(model.getPermissionId());
        if (opt.isEmpty())
            return response(false, "Permission not found", null);

        return response(true, "Permission fetched", permissionMapper.toModel(opt.get()));
    }

    // =====================================================
    // GET ALL
    // =====================================================
    @Override
    public Map<String, Object> getAll() {

        List<PermissionModel> list = permissionRepo.findAll()
                .stream()
                .filter(Permission::isActive)
                .map(permissionMapper::toModel)
                .toList();

        return response(true, "All permissions", list);
    }

    // =====================================================
    // GET BY ROLE
    // =====================================================
    @Override
    public Map<String, Object> getByRoleId(PermissionModel model) {

        if (model == null || model.getRoleId() == null)
            return response(false, "RoleId required", null);

        List<PermissionModel> list = permissionRepo.findAll()
                .stream()
                .filter(p -> p.isActive() &&
                        p.getRole() != null &&
                        p.getRole().getRoleId().equals(model.getRoleId()))
                .map(permissionMapper::toModel)
                .toList();

        return response(true, "Permissions fetched", list);
    }

    @Override
    public Map<String, Object> checkPermission(PermissionModel model) {

        if (model == null)
            return response(false, "Invalid request", null);

        if (!StringUtils.hasText(model.getRoleName()) ||
            !StringUtils.hasText(model.getResourceName()) ||
            !StringUtils.hasText(model.getScopeName())) {

            return response(false, "roleName, resourceName, scopeName required", null);
        }

        Optional<Role> roleOpt = roleRepo.findByRoleNameIgnoreCase(model.getRoleName());
        if (roleOpt.isEmpty())
            return response(false, "Role not found", null);

        Optional<Resource> resourceOpt = resourceRepo.findByResourceNameIgnoreCase(model.getResourceName());
        if (resourceOpt.isEmpty())
            return response(false, "Resource not found", null);

        Optional<Scope> scopeOpt = scopeRepo.findByScopeNameIgnoreCase(model.getScopeName());
        if (scopeOpt.isEmpty())
            return response(false, "Scope not found", null);

        Optional<Permission> permissionOpt =
                permissionRepo.findByRoleAndResourceAndScopeAndActiveTrueAndPermissionStatusTrue(
                        roleOpt.get(),
                        resourceOpt.get(),
                        scopeOpt.get()
                );

        if (permissionOpt.isEmpty())
            return response(false, "Permission denied", false);

        return response(true, "Permission granted", true);
    }

}
