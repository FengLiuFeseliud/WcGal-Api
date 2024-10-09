package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class ArticleInfoDto{
    private Long articleId;
    @NotBlank
    private String articleTitle;
    @NotBlank
    private String articleAuthor;
    @NotBlank
    private String cover;
    private String[] tags;
    private ArticleTagDto[] tagsData;
    protected Date createTime;
    protected Date updateTime;

    public ArticleInfoDto() {}

    public ArticleInfoDto(long articleId) {
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public ArticleTagDto[] getTagsData() {
        return tagsData;
    }

    public void setTagsData(ArticleTagDto[] tagsData) {
        this.tagsData = tagsData;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public @NotBlank String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(@NotBlank String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }
}
