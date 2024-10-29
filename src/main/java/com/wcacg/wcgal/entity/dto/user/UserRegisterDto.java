package com.wcacg.wcgal.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册信息
 */
@Data
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
}
