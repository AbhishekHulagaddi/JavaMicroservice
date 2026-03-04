package com.rim.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.rim.auth.domain.Scope;
import com.rim.auth.model.ScopeModel;

@Component
public class ScopeMapper {
	

    public ScopeModel toModel(Scope scope) {
        if (scope == null) return null;
        ScopeModel model = new ScopeModel();
        model.setScopeId(scope.getScopeId());
        model.setScopeName(scope.getScopeName());
        return model;
    }


    public Scope toDomain(ScopeModel model) {
        if (model == null) return null;
        Scope scope = new Scope();
        scope.setScopeId(model.getScopeId()); 
        scope.setScopeName(model.getScopeName());
        return scope;
    }

    public static void updateDomain(ScopeModel model, Scope scope) {
        if (model == null || scope == null) return;

        scope.setScopeName(model.getScopeName());
        scope.setUpdatedAt(LocalDateTime.now());
    }
}
