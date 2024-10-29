package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailDto {
    @Email
    @NotEmpty
    private String email;
}
