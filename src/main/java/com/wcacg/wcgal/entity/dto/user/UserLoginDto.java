package com.wcacg.wcgal.entity.dto.user;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录信息
 */
public class UserLoginDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
