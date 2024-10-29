package com.wcacg.wcgal.entity.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户登录信息
 */
@Data
public class UserLoginDto {
    @NotBlank
    private String email;

    @Length(min = 8, max = 30)
    @NotBlank
    private String password;
}
