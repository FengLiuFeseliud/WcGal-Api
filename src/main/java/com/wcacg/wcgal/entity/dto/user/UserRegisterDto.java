package com.wcacg.wcgal.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户注册信息
 */
public class UserRegisterDto {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    private String code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String emailCode) {
        this.code = emailCode;
    }
}
