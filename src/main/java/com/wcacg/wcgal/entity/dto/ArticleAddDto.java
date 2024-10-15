package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;

public class ArticleAddDto {
    @NotBlank
    private String articleTitle;
    @NotBlank
    private String articleContent;
    @NotBlank
    private String cover;
    private String[] tags;

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
