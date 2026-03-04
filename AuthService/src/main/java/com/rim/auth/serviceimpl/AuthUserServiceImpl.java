package com.rim.auth.serviceimpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rim.auth.domain.Role;
import com.rim.auth.domain.User;
import com.rim.auth.mapper.AuthUserMapper;
import com.rim.auth.model.CreateUserModel;
import com.rim.auth.model.UserFilterModel;
import com.rim.auth.model.UserModel;
import com.rim.auth.repo.AuthRoleRepo;
import com.rim.auth.repo.AuthUserRepo;
import com.rim.auth.service.AuthUserService;
import com.rim.auth.specifications.UserSpecification;
import com.rim.auth.utils.OtpService;


@Service
public class AuthUserServiceImpl implements AuthUserService{


    @Autowired
    private AuthUserRepo authUserRepo;
    
    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private AuthRoleRepo authRoleRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private OtpService otpService;
    

    private Map<String, Object> response(boolean status, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", data);
        return map;
    }
    
	 // =====================================================
	 // CREATE USER
	 // =====================================================
	 @Override
	 public Map<String, Object> createUser(CreateUserModel model) {
	
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
	
	     if (authUserRepo.existsByEmailIgnoreCase(email))
	         return response(false, "Email already exists", null);
	     // -------------------------------
	     // EMAIL & OTP VALIDATION
	     // -------------------------------
//	     if (model.getOtp() == null) {
//	    	    return response(false, "Please Enter OTP sent to your Email!", null);
//	    	} else {
//
//	    	    Map<String,Object> resp =
//	    	            otpService.verifyOtp(email, model.getOtp().toString());
//
//	    	    Boolean status = (Boolean) resp.get("status");
//
//	    	    if (!status) {
//	    	        return response(false,
//	    	                resp.get("message").toString(),
//	    	                null);
//	    	    }
//	    	}
	
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
	     authUserRepo.save(user);
	
	     return response(true, "User created successfully", authUserMapper.toModel(user));
	 }

    // =====================================================
    // UPDATE USER
    // =====================================================
    @Override
    public Map<String, Object> updateUser(UserModel userModel) {

        if (userModel == null || userModel.getUserId() == null)
            return response(false, "UserId required", null);

        Optional<User> userOpt = authUserRepo.findById(userModel.getUserId());
        if (userOpt.isEmpty())
            return response(false, "User not found", null);

        User user = userOpt.get();

        // email update validation
        if (StringUtils.hasText(userModel.getEmail())) {
            String newEmail = userModel.getEmail().trim();

            Optional<User> existing = authUserRepo.findByEmailIgnoreCase(newEmail);
            if (existing.isPresent() && !existing.get().getUserId().equals(user.getUserId())) {
                return response(false, "Email already used by another user", null);
            }

            user.setEmail(newEmail);
        }

        Role role = null;
        if (userModel.getRoleId() != null) {
            role = authRoleRepo.findById(userModel.getRoleId()).orElse(null);
            if (role == null)
                return response(false, "Role not found", null);
        }

        authUserMapper.updateDomain(userModel, user, role);
        user.setUpdatedAt(LocalDateTime.now());

        authUserRepo.save(user);

        return response(true, "User updated successfully", authUserMapper.toModel(user));
    }

    // =====================================================
    // DELETE USER (SOFT DELETE)
    // =====================================================
    @Override
    public Map<String, Object> deleteUser(UserModel userModel) {

        if (userModel == null || userModel.getUserId() == null)
            return response(false, "UserId required", null);

        Optional<User> userOpt = authUserRepo.findById(userModel.getUserId());
        if (userOpt.isEmpty())
            return response(false, "User not found", null);

        User user = userOpt.get();
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        authUserRepo.save(user);

        return response(true, "User deleted successfully", null);
    }

    // =====================================================
    // GET BY USER ID
    // =====================================================
    @Override
    public Map<String, Object> getUserByUserId(UserModel userModel) {

        if (userModel == null || userModel.getUserId() == null)
            return response(false, "UserId required", null);

        Optional<User> userOpt = authUserRepo.findById(userModel.getUserId());
        if (userOpt.isEmpty())
            return response(false, "User not found", null);

        return response(true, "User fetched", authUserMapper.toModel(userOpt.get()));
    }

    // =====================================================
    // GET BY EMAIL
    // =====================================================
    @Override
    public Map<String, Object> getUserByMailId(UserModel userModel) {

        if (userModel == null || !StringUtils.hasText(userModel.getEmail()))
            return response(false, "Email required", null);

        Optional<User> userOpt = authUserRepo.findByEmailIgnoreCase(userModel.getEmail());
        if (userOpt.isEmpty())
            return response(false, "User not found", null);

        return response(true, "User fetched", authUserMapper.toModel(userOpt.get()));
    }

    // =====================================================
    // GET USERS BY ROLE
    // =====================================================
    @Override
    public Map<String, Object> getUsersByRoleId(UserModel userModel) {

        if (userModel == null || userModel.getRoleId() == null)
            return response(false, "RoleId required", null);

        List<User> users = authUserRepo.findByRole_RoleIdAndActiveTrue(userModel.getRoleId());

        List<UserModel> list = users.stream()
                .map(authUserMapper::toModel)
                .toList();

        return response(true, "Users fetched", list);
    }

    // =====================================================
    // GET ALL USERS
    // =====================================================
    @Override
    public Map<String, Object> getAllUsers() {

        List<UserModel> list = authUserRepo.findAll()
                .stream()
                .filter(User::isActive)
                .map(authUserMapper::toModel)
                .toList();

        return response(true, "All users fetched", list);
    }
    
    // =====================================================
    // GET ALL USERS WITH FILTERS
    // =====================================================
    @Override
    public Map<String, Object> getAllUsersWithFilter(UserFilterModel filter) {

        int page = filter != null ? filter.getPage() : 0;
        int size = filter != null ? filter.getSize() : 10;
        int pageNumber = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("createdAt").descending());

        Specification<User> spec = UserSpecification.filter(filter);

        Page<User> resultPage = authUserRepo.findAll(spec, pageable);

        List<UserModel> users = resultPage.getContent()
                .stream()
                .map(authUserMapper::toModel)
                .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("content", users);
        data.put("currentPage", resultPage.getNumber());
        data.put("totalItems", resultPage.getTotalElements());
        data.put("totalPages", resultPage.getTotalPages());

        return response(true, "Users fetched", data);
    }

}
