package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleTagDto {
    private Integer tagId;
    @NotBlank
    private String tagName;
    private Integer tagCount;
}
