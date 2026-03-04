package com.rim.auth.service;

import java.util.Map;

import com.rim.auth.model.ScopeModel;

public interface ScopeService {

	public Map<String , Object> createScope (ScopeModel scopeModel);
	
	public Map<String , Object> updateScope (ScopeModel scopeModel);
	
	public Map<String , Object> deleteScope (ScopeModel scopeModel);
	
	public Map<String , Object> getScopeByScopeId (ScopeModel scopeModel);
	
	public Map<String , Object> getAllScopes ();
}
