package com.rim.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rim.auth.model.ForgotPasswordRequest;
import com.rim.auth.model.LoginRequest;
import com.rim.auth.model.OTPRequest;
import com.rim.auth.model.RefreshTokenRequest;
import com.rim.auth.model.ResetPasswordRequest;
import com.rim.auth.service.AuthenticationService;
import com.rim.auth.utils.OtpService;
import com.rim.auth.utils.WebConstantUrl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(WebConstantUrl.AUTHENTICATE)
public class AuthenticationController {

	    @Autowired
	    private AuthenticationService authService;
	    
	    @Autowired
	    private OtpService otpService;

	    @PostMapping(WebConstantUrl.SIGN_IN)
	    public Map<String,Object> signIn(@RequestBody LoginRequest req){
	        return authService.signIn(req);
	    }

	    @PostMapping(WebConstantUrl.FORGOT_PASSWORD)
	    public Map<String,Object> forgot(@RequestBody ForgotPasswordRequest req){
	        return authService.forgotPassword(req);
	    }

	    @PostMapping(WebConstantUrl.RESET_PASSWORD)
	    public Map<String,Object> reset(@RequestBody ResetPasswordRequest req){
	        return authService.resetPassword(req);
	    }

	    @PostMapping(WebConstantUrl.REFRESH_TOKEN)
	    public Map<String,Object> refresh(@RequestBody RefreshTokenRequest req){
	        return authService.refresh(req);
	    }
	    
	    @PostMapping(WebConstantUrl.GENERATE_OTP)
	    public Map<String,Object> sendOtp(@RequestBody OTPRequest req)
	            throws Exception {
	    	return otpService.sendOtp(req);
	    }
	    
	    @PostMapping(WebConstantUrl.LOGOUT)
	    public Map<String,Object> logout(HttpServletRequest request,
	            @RequestBody(required = false) RefreshTokenRequest req) {

	        String authHeader = request.getHeader("Authorization");
	        return authService.logout(authHeader, req);
	    }

	}
