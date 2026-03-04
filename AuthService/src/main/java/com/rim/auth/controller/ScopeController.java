package com.rim.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rim.auth.model.ScopeModel;
import com.rim.auth.service.ScopeService;
import com.rim.auth.utils.WebConstantUrl;


@RestController
@RequestMapping(WebConstantUrl.SCOPE)
public class ScopeController {

    @Autowired
    private ScopeService scopeService;

    // ============================================
    // CREATE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.CREATE)
    public Map<String, Object> createScope(@RequestBody ScopeModel scopeModel) {
        return scopeService.createScope(scopeModel);
    }

    // ============================================
    // UPDATE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.UPDATE)
    public Map<String, Object> updateScope(@RequestBody ScopeModel scopeModel) {
        return scopeService.updateScope(scopeModel);
    }

    // ============================================
    // DELETE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.DELETE)
    public Map<String, Object> deleteScope(@RequestBody ScopeModel scopeModel) {
        return scopeService.deleteScope(scopeModel);
    }

    // ============================================
    // GET ROLE BY ID
    // ============================================
    @PostMapping(WebConstantUrl.GET_BY_ID)
    public Map<String, Object> getScopeById(@RequestBody ScopeModel scopeModel) {
        return scopeService.getScopeByScopeId(scopeModel);
    }

    // ============================================
    // GET ALL ROLES
    // ============================================
    @PostMapping(WebConstantUrl.GET_ALL)
    public Map<String, Object> getAllScopes() {
        return scopeService.getAllScopes();
    }
}
