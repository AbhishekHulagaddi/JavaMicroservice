package com.rim.auth.service;

import java.util.Map;

import com.rim.auth.model.RoleModel;

public interface AuthRoleService {

	public Map<String , Object> createRole (RoleModel roleModel);
	
	public Map<String , Object> updateRole (RoleModel roleModel);
	
	public Map<String , Object> deleteRole (RoleModel roleModel);
	
	public Map<String , Object> getRoleByRoleId (RoleModel roleModel);
	
	public Map<String , Object> getAllRoles ();
}
