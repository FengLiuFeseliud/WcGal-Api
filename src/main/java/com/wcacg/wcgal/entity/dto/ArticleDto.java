package com.wcacg.wcgal.entity.dto;

import com.wcacg.wcgal.entity.Comment;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import jakarta.validation.constraints.NotBlank;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.Date;
import java.util.List;

public class ArticleDto {
    private Long articleId;
    private String articleTitle;
    private UserInfoDto articleAuthor;
    private String articleContent;
    private String cover;
    private String[] tags;
    private ArticleTagDto[] tagsData;
    protected Date createTime;
    protected Date updateTime;
    private Long comments;
    private Long likes;
    private Long views;
    private Long favorites;
    private List<Comment> commentList;

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

    public @NotBlank UserInfoDto getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(@NotBlank UserInfoDto articleAuthor) {
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

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getFavorites() {
        return favorites;
    }

    public void setFavorites(Long favorites) {
        this.favorites = favorites;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
