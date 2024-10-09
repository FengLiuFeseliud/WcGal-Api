package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDto extends EmailDto{
    @NotBlank
    private String password;

    @NotBlank
    private String code;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
