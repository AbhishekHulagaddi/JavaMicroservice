package com.rim.auth.model;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String token;
    private String newPassword;
    private String confirmPassword;
}
