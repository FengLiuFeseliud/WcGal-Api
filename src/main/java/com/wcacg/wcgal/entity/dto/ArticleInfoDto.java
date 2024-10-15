package com.wcacg.wcgal.entity.dto;

import com.wcacg.wcgal.entity.dto.user.UserInfoDto;

import java.util.Date;

public class ArticleInfoDto{
    private Long articleId;
    private String articleTitle;
    private UserInfoDto articleAuthor;
    private String cover;
    private String[] tags;
    private ArticleTagDto[] tagsData;
    protected Date createTime;
    protected Date updateTime;
    private Long comments;
    private Long likes;
    private Long views;
    private Long favorites;

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

    public UserInfoDto getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(UserInfoDto articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getFavorites() {
        return favorites;
    }

    public void setFavorites(Long favorites) {
        this.favorites = favorites;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }
}
