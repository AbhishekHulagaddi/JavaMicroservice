package com.rim.auth.controller;

import com.rim.auth.utils.WebConstantUrl;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rim.auth.model.CreateUserModel;
import com.rim.auth.model.UserFilterModel;
import com.rim.auth.model.UserModel;
import com.rim.auth.service.AuthUserService;


@RestController
@RequestMapping(WebConstantUrl.USER)
public class AuthUserController {

    @Autowired
    private AuthUserService authUserService;


    @PostMapping(WebConstantUrl.CREATE)
    public Map<String, Object> createUser(@RequestBody CreateUserModel model) {
        return authUserService.createUser(model);
    }
    // =====================================================
    // UPDATE USER
    // =====================================================
    @PostMapping(WebConstantUrl.UPDATE)
    public Map<String, Object> updateUser(@RequestBody UserModel model) {
        return authUserService.updateUser(model);
    }

    // =====================================================
    // DELETE USER
    // =====================================================
    @PostMapping(WebConstantUrl.DELETE)
    public Map<String, Object> deleteUser(@RequestBody UserModel model) {
        return authUserService.deleteUser(model);
    }

    // =====================================================
    // GET USER BY ID
    // =====================================================
    @PostMapping(WebConstantUrl.GET_BY_ID)
    public Map<String, Object> getUserById(@RequestBody UserModel model) {
        return authUserService.getUserByUserId(model);
    }

    // =====================================================
    // GET USER BY EMAIL
    // =====================================================
    @PostMapping(WebConstantUrl.GET_BY_EMAIL)
    public Map<String, Object> getUserByEmail(@RequestBody UserModel model) {
        return authUserService.getUserByMailId(model);
    }

    // =====================================================
    // GET USERS BY ROLE
    // =====================================================
    @PostMapping(WebConstantUrl.GET_BY_ROLENAME)
    public Map<String, Object> getUsersByRole(@RequestBody UserModel model) {
        return authUserService.getUsersByRoleId(model);
    }

    // =====================================================
    // GET ALL USERS
    // =====================================================
    @PostMapping(WebConstantUrl.GET_ALL)
    public Map<String, Object> getAllUsers() {
        return authUserService.getAllUsers();
    }
    // =====================================================
    // GET ALL USERS WITH FILTER
    // =====================================================
    @PostMapping(WebConstantUrl.GET_ALL_FILTER)
    public Map<String, Object> getAllUsersWithFilter(@RequestBody UserFilterModel filter) {
        return authUserService.getAllUsersWithFilter(filter);
    }

}
