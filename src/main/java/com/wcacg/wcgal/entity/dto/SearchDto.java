package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class SearchDto extends PageDto{
    @Length(min = 1, max = 200)
    @NotBlank
    private String keyword;

    public String getKeyword() {
        return keyword;
    }
}
