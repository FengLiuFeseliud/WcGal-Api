package com.wcacg.wcgal.entity.dto.favorite;

import com.wcacg.wcgal.entity.dto.article.ArticleInfoDto;

import java.util.Date;

public class FavoriteItemDto {
    private Long favoriteId;
    private Long favoriteUserId;
    private String resourceId;
    private ArticleInfoDto article;
    protected Date createTime;

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Long getFavoriteUserId() {
        return favoriteUserId;
    }

    public void setFavoriteUserId(Long favoriteUserId) {
        this.favoriteUserId = favoriteUserId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public ArticleInfoDto getArticle() {
        return article;
    }

    public void setArticle(ArticleInfoDto article) {
        this.article = article;
    }
}
