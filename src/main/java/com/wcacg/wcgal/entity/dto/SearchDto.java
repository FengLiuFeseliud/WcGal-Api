package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;

public class SearchDto extends PageDto{
    @NotBlank
    private String keyword;

    public String getKeyword() {
        return keyword;
    }
}
