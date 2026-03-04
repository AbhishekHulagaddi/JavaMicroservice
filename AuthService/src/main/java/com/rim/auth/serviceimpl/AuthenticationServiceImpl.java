package com.rim.auth.serviceimpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rim.auth.domain.PasswordResetToken;
import com.rim.auth.domain.RefreshToken;
import com.rim.auth.domain.Role;
import com.rim.auth.domain.User;
import com.rim.auth.mapper.AuthUserMapper;
import com.rim.auth.model.AuthResponse;
import com.rim.auth.model.CreateUserModel;
import com.rim.auth.model.ForgotPasswordRequest;
import com.rim.auth.model.LoginRequest;
import com.rim.auth.model.RefreshTokenRequest;
import com.rim.auth.model.ResetPasswordRequest;
import com.rim.auth.repo.AuthRoleRepo;
import com.rim.auth.repo.AuthUserRepo;
import com.rim.auth.repo.PasswordResetTokenRepo;
import com.rim.auth.repo.RefreshTokenRepo;
import com.rim.auth.service.AuthenticationService;
import com.rim.auth.utils.GatewayConfig;
import com.rim.auth.utils.JwtUtil;
import com.rim.auth.utils.OtpService;

import jakarta.transaction.Transactional;
@Service
public class AuthenticationServiceImpl implements AuthenticationService{


    @Autowired
    private AuthUserRepo userRepo;
    
    @Autowired
    private RefreshTokenRepo refreshRepo;

    @Autowired
    private PasswordResetTokenRepo resetRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private GatewayConfig gatewayConfig;
    
    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private AuthRoleRepo authRoleRepo;
    
    @Autowired
    private OtpService otpService;
    




    private Map<String,Object> response(boolean status,String msg,Object data){
        Map<String,Object> map=new HashMap<>();
        map.put("status",status);
        map.put("message",msg);
        map.put("data",data);
        return map;
    }
    
    
	 // =====================================================
	 // SIGNUP USER
	 // =====================================================
	 @Override
	 public Map<String, Object> signUp (CreateUserModel model) {
	
	     // -------------------------------
	     // NULL CHECK
	     // -------------------------------
	     if (model == null)
	         return response(false, "Request body missing", null);
	
	     // -------------------------------
	     // EMAIL VALIDATION
	     // -------------------------------
	     if (!StringUtils.hasText(model.getEmail()))
	         return response(false, "Email is required", null);
	
	     String email = model.getEmail().trim().toLowerCase();
	
	     if (userRepo.existsByEmailIgnoreCase(email))
	         return response(false, "Email already exists", null);
	     // -------------------------------
	     // EMAIL & OTP VALIDATION
	     // -------------------------------
	     if (model.getOtp() == null) {
	    	    return response(false, "Please Enter OTP sent to your Email!", null);
	    	} else {

	    	    Map<String,Object> resp =
	    	            otpService.verifyOtp(email, model.getOtp().toString());

	    	    Boolean status = (Boolean) resp.get("status");

	    	    if (!status) {
	    	        return response(false,
	    	                resp.get("message").toString(),
	    	                null);
	    	    }
	    	}
	
	     // -------------------------------
	     // PASSWORD VALIDATION
	     // -------------------------------
	     if (!StringUtils.hasText(model.getPassword()))
	         return response(false, "Password is required", null);
	
	     if (!StringUtils.hasText(model.getConfirmPassword()))
	         return response(false, "Confirm password is required", null);
	
	     if (!model.getPassword().equals(model.getConfirmPassword()))
	         return response(false, "Password and Confirm Password mismatch", null);
	
	     // password length check
	     if (model.getPassword().length() < 6)
	         return response(false, "Password must be at least 6 characters", null);
	
	     // -------------------------------
	     // ROLE VALIDATION
	     // -------------------------------
	     if (model.getRoleId() == null)
	         return response(false, "RoleId is required", null);
	
	     Optional<Role> roleOpt = authRoleRepo.findById(model.getRoleId());
	     if (roleOpt.isEmpty())
	         return response(false, "Role not found", null);
	
	     Role role = roleOpt.get();
	
	     // -------------------------------
	     // PASSWORD ENCODE
	     // -------------------------------
	     String encodedPassword = passwordEncoder.encode(model.getPassword());
	
	     // -------------------------------
	     // MAP TO ENTITY
	     // -------------------------------
	     User user = authUserMapper.toDomain(model, role, encodedPassword);
	     user.setCreatedAt(LocalDateTime.now());
	     user.setEmailVerified(true);
	     userRepo.save(user);
	
	     return response(true, "User created successfully", authUserMapper.toModel(user));
	 }

    
    
    

    // =====================================================
    // SIGN IN
    // =====================================================
    @Transactional 
    @Override
    public Map<String,Object> signIn(LoginRequest req){

    // ===============================
    // BASIC REQUEST VALIDATION
    // ===============================
    if(req == null)
        return response(false,"Request body missing",null);

    if(req.getEmail() == null || req.getEmail().trim().isEmpty())
        return response(false,"Email required",null);

    if(req.getPassword() == null || req.getPassword().trim().isEmpty())
        return response(false,"Password required",null);

    // ===============================
    // USER FETCH
    // ===============================
    Optional<User> opt = userRepo.findByEmailIgnoreCase(req.getEmail().trim());

    if(opt.isEmpty())
        return response(false,"Invalid credentials",null);

    User user = opt.get();

    // ===============================
    // ACCOUNT STATUS VALIDATIONS
    // ===============================
    if(!user.isActive())
        return response(false,"Account disabled",null);

    if(!user.isAccountNonLocked())
        return response(false,"Account locked",null);

    if(user.getPassword() == null)
        return response(false,"Password not set. Contact admin",null);
    
    if(!user.isEmailVerified())
        return response(false,"Please verify your Email before Login!",null);


    // ===============================
    // PASSWORD MATCH
    // ===============================
    if(!passwordEncoder.matches(req.getPassword(), user.getPassword()))
        return response(false,"Invalid credentials",null);

    // ===============================
    // GENERATE TOKENS
    // ===============================
    String accessToken  = jwtUtil.generateAccessToken(user);
    String refreshToken = jwtUtil.generateRefreshToken(user);

    // ===============================
    // SINGLE DEVICE LOGIN
    // remove all old refresh tokens
    // ===============================
    refreshRepo.deleteByUser(user);

    // ===============================
    // SAVE NEW REFRESH TOKEN
    // ===============================
    RefreshToken entity = new RefreshToken();
    entity.setToken(refreshToken);
    entity.setUser(user);
    entity.setExpiryDate(LocalDateTime.now().plusDays(7)); 
    entity.setRevoked(false);

    refreshRepo.save(entity);

    // ===============================
    // RESPONSE
    // ===============================
    AuthResponse res = new AuthResponse(
            accessToken,
            refreshToken,
            user.getEmail(),
            user.getRole().getRoleName()
    );

    return response(true,"Login success",res);

    }



    // =====================================================
    // FORGOT PASSWORD
    // =====================================================
    public Map<String,Object> forgotPassword(ForgotPasswordRequest req){

        if(req==null || req.getEmail()==null)
            return response(false,"Email required",null);

        Optional<User> opt=userRepo.findByEmailIgnoreCase(req.getEmail());

        if(opt.isEmpty())
            return response(true,"If email exists reset link sent",null);

        String token=UUID.randomUUID().toString();
        String hash=passwordEncoder.encode(token);

        PasswordResetToken entity=new PasswordResetToken();
        entity.setEmail(req.getEmail());
        entity.setTokenHash(hash);
        entity.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        entity.setUsed(false);

        resetRepo.save(entity);

        // TODO: send email service
        System.out.println("RESET TOKEN: "+token);

        return response(true,"Reset token sent",null);
    }


    // =====================================================
    // RESET PASSWORD
    // =====================================================
    public Map<String,Object> resetPassword(ResetPasswordRequest req){

        if(req.getEmail()==null || req.getToken()==null)
            return response(false,"Invalid request",null);

        if(!req.getNewPassword().equals(req.getConfirmPassword()))
            return response(false,"Password mismatch",null);

        List<PasswordResetToken> tokens =
                resetRepo.findByEmailAndUsedFalse(req.getEmail());

        for(PasswordResetToken t:tokens){

            if(t.getExpiryDate().isBefore(LocalDateTime.now()))
                continue;

            if(passwordEncoder.matches(req.getToken(), t.getTokenHash())){

                User user=userRepo.findByEmailIgnoreCase(req.getEmail()).orElseThrow();
                user.setPassword(passwordEncoder.encode(req.getNewPassword()));
                userRepo.save(user);

                t.setUsed(true);
                resetRepo.save(t);

                return response(true,"Password reset success",null);
            }
        }

        return response(false,"Invalid or expired token",null);
    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================
    public Map<String,Object> refresh(RefreshTokenRequest req){

    	
    	if(req.getRefreshToken()==null)
    	    return response(false,"Refresh token required",null);

    	Optional<RefreshToken> tokenOpt =
    	        refreshRepo.findByTokenAndRevokedFalse(req.getRefreshToken());

    	if(tokenOpt.isEmpty())
    	    return response(false,"Invalid refresh token",null);

    	RefreshToken storedToken = tokenOpt.get();

    	// check revoked
    	if(storedToken.isRevoked())
    	    return response(false,"Refresh token already used",null);

    	// check expiry from DB
    	if(storedToken.getExpiryDate().isBefore(LocalDateTime.now()))
    	    return response(false,"Refresh token expired",null);

    	User user = storedToken.getUser();

    	if(!user.isActive())
    	    return response(false,"User disabled",null);

    	// revoke old refresh token
    	storedToken.setRevoked(true);
    	refreshRepo.save(storedToken);

    	// generate new tokens
    	String newAccess = jwtUtil.generateAccessToken(user);
    	String newRefresh = jwtUtil.generateRefreshToken(user);

    	// save new refresh token
    	RefreshToken newToken = new RefreshToken(
    	        user,
    	        newRefresh,
    	        LocalDateTime.now().plusDays(7)
    	);

    	refreshRepo.save(newToken);

    	Map<String,String> data=new HashMap<>();
    	data.put("accessToken",newAccess);
    	data.put("refreshToken",newRefresh);

    	return response(true,"Token refreshed",data);

    	}
    @Override
    @Transactional
    public Map<String,Object> logout(String authHeader, RefreshTokenRequest req){

        if(authHeader == null || !authHeader.startsWith("Bearer "))
            return response(false,"Access token missing",null);

        String accessToken = authHeader.substring(7);

        // =================================
        // REVOKE REFRESH TOKEN
        // =================================
        if(req.getRefreshToken()!=null){
            refreshRepo.findByTokenAndRevokedFalse(req.getRefreshToken())
                    .ifPresent(token -> {
                        token.setRevoked(true);
                        refreshRepo.save(token);
                    });
        }

        // =================================
        // CALL GATEWAY TO BLOCK ACCESS TOKEN
        // =================================
        try{
        	gatewayConfig.blockToken(Map.of("token", accessToken));
        }catch(Exception e){
            System.out.println("Gateway block failed: " + e.getMessage());
        }

        return response(true,"Logged out successfully",null);
    }




}
