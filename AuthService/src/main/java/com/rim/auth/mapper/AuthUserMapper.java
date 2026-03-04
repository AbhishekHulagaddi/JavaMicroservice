package com.rim.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.rim.auth.domain.Role;
import com.rim.auth.domain.User;
import com.rim.auth.model.CreateUserModel;
import com.rim.auth.model.UserModel;

@Component
public class AuthUserMapper {
	
    // =====================================================
    // DOMAIN → DTO
    // =====================================================
    public UserModel toModel(User user) {
        if (user == null) return null;

        UserModel model = new UserModel();
        model.setUserId(user.getUserId());
        model.setEmail(user.getEmail());
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setPhone(user.getPhone());
        model.setActive(user.isActive());
        model.setEmailVerified(user.isEmailVerified());

        if (user.getRole() != null) {
            model.setRoleId(user.getRole().getRoleId());
            model.setRoleName(user.getRole().getRoleName());
        }

        return model;
    }

    // =====================================================
    // DTO → DOMAIN (CREATE)
    // =====================================================
    public User toDomain(UserModel model, Role role, String encodedPassword) {
        if (model == null) return null;

        User user = new User();
        user.setUserId(model.getUserId()); // for update case if needed
        user.setEmail(model.getEmail());
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setPhone(model.getPhone());
        user.setActive(model.isActive());
        user.setEmailVerified(model.isEmailVerified());
        user.setRole(role);
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    // =====================================================
    // UPDATE DOMAIN FROM DTO
    // =====================================================
    public void updateDomain(UserModel model, User user, Role role) {
        if (model == null || user == null) return;

        if (model.getFirstName() != null)
            user.setFirstName(model.getFirstName());

        if (model.getLastName() != null)
            user.setLastName(model.getLastName());

        if (model.getPhone() != null)
            user.setPhone(model.getPhone());

        if (role != null)
            user.setRole(role);

        user.setActive(model.isActive());
        user.setEmailVerified(model.isEmailVerified());
        user.setUpdatedAt(LocalDateTime.now());
    }

	 // =====================================================
	 // CREATE USER FROM CreateUserModel
	 // =====================================================
	 public User toDomain(CreateUserModel model, Role role, String encodedPassword) {
	
	     if (model == null) return null;
	
	     User user = new User();
	     user.setEmail(model.getEmail().trim().toLowerCase());
	     user.setFirstName(model.getFirstName());
	     user.setLastName(model.getLastName());
	     user.setPhone(model.getPhone());
	     user.setActive(true);
	     user.setAccountNonLocked(true);
	     user.setEmailVerified(false);
	     user.setPassword(encodedPassword);
	     user.setRole(role);
	     user.setCreatedAt(LocalDateTime.now());
	
	     return user;
 }

}
