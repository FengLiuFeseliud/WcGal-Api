package com.wcacg.wcgal.entity.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ArticleDto {
    private Long articleId;
    @NotBlank
    private String articleTitle;
    @NotBlank
    private String articleAuthor;
    @NotBlank
    private String articleContent;
    @NotBlank
    private String cover;
    private String[] tags;
    private ArticleTagDto[] tagsData;
    protected Date createTime;
    protected Date updateTime;

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

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public ArticleTagDto[] getTagsData() {
        return tagsData;
    }

    public void setTagsData(ArticleTagDto[] tagsData) {
        this.tagsData = tagsData;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTags() {
        return StringUtils.join(tags);
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
