package com.rim.auth.serviceimpl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rim.auth.domain.Scope;
import com.rim.auth.mapper.ScopeMapper;
import com.rim.auth.model.ScopeModel;
import com.rim.auth.repo.ScopeRepo;
import com.rim.auth.service.ScopeService;


@Service
public class ScopeServiceImpl implements ScopeService{


    @Autowired
    private ScopeRepo scopeRepository;
    
    @Autowired
    private ScopeMapper scopeMapper;

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
    public Map<String, Object> createScope(ScopeModel scopeModel) {

        if (scopeModel == null || !StringUtils.hasText(scopeModel.getScopeName())) {
            return response(false, "Scope name is required", null);
        }

        String scopeName = scopeModel.getScopeName().trim();

        if (scopeRepository.existsByScopeNameIgnoreCase(scopeName)) {
            return response(false, "Scope already exists", null);
        }

        Scope scope = scopeMapper.toDomain(scopeModel);
        scope.setCreatedAt(LocalDateTime.now());
        scope.setActive(true);

        scopeRepository.save(scope);

        return response(true, "Scope created successfully", scopeMapper.toModel(scope));
    }

    // =====================================================
    // UPDATE ROLE
    // =====================================================
    @Override
    public Map<String, Object> updateScope(ScopeModel scopeModel) {

        if (scopeModel == null || scopeModel.getScopeId() == null) {
            return response(false, "ScopeId is required", null);
        }

        Optional<Scope> optional = scopeRepository.findById(scopeModel.getScopeId());
        if (optional.isEmpty()) {
            return response(false, "Scope not found", null);
        }

        Scope scope = optional.get();

        if (StringUtils.hasText(scopeModel.getScopeName())) {

            String newName = scopeModel.getScopeName().trim();

            Optional<Scope> existing = scopeRepository.findByScopeNameIgnoreCase(newName);
            if (existing.isPresent() && !existing.get().getScopeId().equals(scope.getScopeId())) {
                return response(false, "Another scope with same name exists", null);
            }

            scope.setScopeName(newName);
        }

        scope.setUpdatedAt(LocalDateTime.now());
        scopeRepository.save(scope);

        return response(true, "Scope updated successfully", scopeMapper.toModel(scope));
    }

    // =====================================================
    // DELETE ROLE (Soft Delete)
    // =====================================================
    @Override
    public Map<String, Object> deleteScope(ScopeModel scopeModel) {

        if (scopeModel == null || scopeModel.getScopeId() == null) {
            return response(false, "ScopeId is required", null);
        }

        Optional<Scope> optional = scopeRepository.findById(scopeModel.getScopeId());
        if (optional.isEmpty()) {
            return response(false, "Scope not found", null);
        }

        Scope scope = optional.get();
        scope.setActive(false);
        scope.setUpdatedAt(LocalDateTime.now());

        scopeRepository.save(scope);

        return response(true, "Scope deleted successfully", null);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @Override
    public Map<String, Object> getScopeByScopeId(ScopeModel scopeModel) {

        if (scopeModel == null || scopeModel.getScopeId() == null) {
            return response(false, "ScopeId is required", null);
        }

        Optional<Scope> optional = scopeRepository.findById(scopeModel.getScopeId());
        if (optional.isEmpty()) {
            return response(false, "Scope not found", null);
        }

        return response(true, "Scope fetched", scopeMapper.toModel(optional.get()));
    }

    // =====================================================
    // GET ALL
    // =====================================================
    @Override
    public Map<String, Object> getAllScopes() {

        List<Scope> scopes = scopeRepository.findAll();

        List<ScopeModel> list = scopes.stream()
                .filter(Scope::isActive)
                .map(scopeMapper::toModel)
                .toList();

        return response(true, "All scopes fetched", list);
    }
}
