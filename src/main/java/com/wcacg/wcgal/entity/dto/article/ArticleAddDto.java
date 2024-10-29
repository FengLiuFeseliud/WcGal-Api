package com.wcacg.wcgal.entity.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class ArticleAddDto {
    private Long articleId;

    @NotBlank
    @Length(min = 1, max = 50)
    private String articleTitle;

    @NotBlank
    private String articleContent;

    @URL
    @NotBlank
    private String cover;

    @Size(max = 30)
    private String[] tags;
}
