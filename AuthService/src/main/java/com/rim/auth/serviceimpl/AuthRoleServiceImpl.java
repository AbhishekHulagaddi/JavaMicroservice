package com.rim.auth.serviceimpl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rim.auth.domain.Role;
import com.rim.auth.mapper.AuthRoleMapper;
import com.rim.auth.model.RoleModel;
import com.rim.auth.repo.AuthRoleRepo;
import com.rim.auth.service.AuthRoleService;

@Service
public class AuthRoleServiceImpl implements AuthRoleService{


    @Autowired
    private AuthRoleRepo roleRepository;
    
    @Autowired
    private AuthRoleMapper roleMapper;

    private Map<String, Object> response(boolean status, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", data);
        return map;
    }

    // =====================================================
    // CREATE ROLE
    // =====================================================
    @Override
    public Map<String, Object> createRole(RoleModel roleModel) {

        if (roleModel == null || !StringUtils.hasText(roleModel.getRoleName())) {
            return response(false, "Role name is required", null);
        }

        String roleName = roleModel.getRoleName().trim();

        if (roleRepository.existsByRoleNameIgnoreCase(roleName)) {
            return response(false, "Role already exists", null);
        }

        Role role = roleMapper.toDomain(roleModel);
        role.setCreatedAt(LocalDateTime.now());
        role.setActive(true);

        roleRepository.save(role);

        return response(true, "Role created successfully", roleMapper.toModel(role));
    }

    // =====================================================
    // UPDATE ROLE
    // =====================================================
    @Override
    public Map<String, Object> updateRole(RoleModel roleModel) {

        if (roleModel == null || roleModel.getRoleId() == null) {
            return response(false, "RoleId is required", null);
        }

        Optional<Role> optional = roleRepository.findById(roleModel.getRoleId());
        if (optional.isEmpty()) {
            return response(false, "Role not found", null);
        }

        Role role = optional.get();

        if (StringUtils.hasText(roleModel.getRoleName())) {

            String newName = roleModel.getRoleName().trim();

            Optional<Role> existing = roleRepository.findByRoleNameIgnoreCase(newName);
            if (existing.isPresent() && !existing.get().getRoleId().equals(role.getRoleId())) {
                return response(false, "Another role with same name exists", null);
            }

            role.setRoleName(newName);
        }

        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);

        return response(true, "Role updated successfully", roleMapper.toModel(role));
    }

    // =====================================================
    // DELETE ROLE (Soft Delete)
    // =====================================================
    @Override
    public Map<String, Object> deleteRole(RoleModel roleModel) {

        if (roleModel == null || roleModel.getRoleId() == null) {
            return response(false, "RoleId is required", null);
        }

        Optional<Role> optional = roleRepository.findById(roleModel.getRoleId());
        if (optional.isEmpty()) {
            return response(false, "Role not found", null);
        }

        Role role = optional.get();
        role.setActive(false);
        role.setUpdatedAt(LocalDateTime.now());

        roleRepository.save(role);

        return response(true, "Role deleted successfully", null);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @Override
    public Map<String, Object> getRoleByRoleId(RoleModel roleModel) {

        if (roleModel == null || roleModel.getRoleId() == null) {
            return response(false, "RoleId is required", null);
        }

        Optional<Role> optional = roleRepository.findById(roleModel.getRoleId());
        if (optional.isEmpty()) {
            return response(false, "Role not found", null);
        }

        return response(true, "Role fetched", roleMapper.toModel(optional.get()));
    }

    // =====================================================
    // GET ALL
    // =====================================================
    @Override
    public Map<String, Object> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        List<RoleModel> list = roles.stream()
                .filter(Role::isActive)
                .map(roleMapper::toModel)
                .toList();

        return response(true, "All roles fetched", list);
    }
}
