package com.wcacg.wcgal.entity.dto.user;

import com.wcacg.wcgal.entity.dto.EmailDto;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class ResetPasswordDto extends EmailDto {
    @Length(min = 8, max = 30)
    @NotBlank
    private String password;

    @Length(min = 6, max = 6)
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
