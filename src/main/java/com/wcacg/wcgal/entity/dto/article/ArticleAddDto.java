package com.wcacg.wcgal.entity.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

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

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public @NotBlank String getCover() {
        return cover;
    }

    public void setCover(@NotBlank String cover) {
        this.cover = cover;
    }

    public @NotBlank String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(@NotBlank String articleContent) {
        this.articleContent = articleContent;
    }

    public @NotBlank String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(@NotBlank String articleTitle) {
        this.articleTitle = articleTitle;
    }
}
