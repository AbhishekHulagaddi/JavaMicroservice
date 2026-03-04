package com.rim.auth.utils;

public interface WebConstantUrl {

	//Resources
	public static final String USER = "/user";
	public static final String ROLE = "/role";
	public static final String AUTH = "/auth";
	public static final String AUTHENTICATE = "/authenticate";
	public static final String ACCESS_POLICY = "/accesspolicy";
	
	public static final String SCOPE = "/scopes";
	public static final String RESOURCE = "/resource";
	public static final String PERMISSION = "/permission";
	
	//Scopes
	public static final String CREATE = "/create";
	public static final String UPDATE = "/update";
	public static final String DELETE = "/delete";
	public static final String GET_BY_ID = "/getById";
	public static final String GET_ALL = "/getAll";
	public static final String GET_ALL_FILTER = "/getAllWithFilter";

	public static final String GET_BY_ROLEID = "/getByRole";
	
	
	//UNIVERSAL ACCESS

	public static final String REGISTER = "/register";
	public static final String SIGN_IN="/signin";
	public static final String SIGN_UP="/signup";
	public static final String LOGOUT="/logout";
	public static final String FORGOT_PASSWORD="/forgotPassword";
	public static final String RESET_PASSWORD="/resetPassword";
	public static final String REFRESH_TOKEN="/refreshToken";
	public static final String GENERATE_OTP="/generateOtp";
	
	public static final String GET_BY_ROLENAME = "/getByRole";
	public static final String GET_BY_EMAIL = "/getByEmail";
	public static final String CHECK = "/check";
	
	
}
