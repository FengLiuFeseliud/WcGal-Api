package com.wcacg.wcgal.entity.dto.user;

import com.wcacg.wcgal.entity.dto.EmailDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;


@Data
@EqualsAndHashCode(callSuper = true)
public class ResetPasswordDto extends EmailDto {
    @Length(min = 8, max = 30)
    @NotBlank
    private String password;

    @Length(min = 6, max = 6)
    @NotBlank
    private String code;
}
