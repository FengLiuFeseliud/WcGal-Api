package com.wcacg.wcgal.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册信息
 */
public class UserRegisterDto {
    @Length(min = 2, max = 30)
    @NotBlank
    private String userName;

    @Length(min = 8, max = 30)
    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @Length(min = 6, max = 6)
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
