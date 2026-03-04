package com.rim.auth.service;

import java.util.Map;

import com.rim.auth.model.CreateUserModel;
import com.rim.auth.model.UserFilterModel;
import com.rim.auth.model.UserModel;

public interface AuthUserService {

	public Map<String, Object> createUser(CreateUserModel model);
	
	public Map<String , Object> updateUser (UserModel userModel);
	
	public Map<String , Object> deleteUser (UserModel userModel);
	
	public Map<String , Object> getUserByUserId (UserModel userModel);
	
	public Map<String , Object> getUserByMailId (UserModel userModel);
	
	public Map<String , Object> getUsersByRoleId (UserModel userModel);
	
	public Map<String , Object> getAllUsers ();
	
	public Map<String, Object> getAllUsersWithFilter(UserFilterModel filter);

}
