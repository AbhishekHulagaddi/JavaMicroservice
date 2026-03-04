package com.rim.auth.service;

import java.util.Map;

import com.rim.auth.model.CreateUserModel;
import com.rim.auth.model.ForgotPasswordRequest;
import com.rim.auth.model.LoginRequest;
import com.rim.auth.model.RefreshTokenRequest;
import com.rim.auth.model.ResetPasswordRequest;

public interface AuthenticationService {


	
	 	public Map<String,Object> signIn(LoginRequest req);
	 	
		public Map<String, Object> signUp(CreateUserModel model);

	    public Map<String,Object> forgotPassword(ForgotPasswordRequest req);

	    public Map<String,Object> resetPassword(ResetPasswordRequest req);

	    public Map<String,Object> refresh(RefreshTokenRequest req);
	    
	    public Map<String,Object> logout(String authHeader, RefreshTokenRequest req);
}
