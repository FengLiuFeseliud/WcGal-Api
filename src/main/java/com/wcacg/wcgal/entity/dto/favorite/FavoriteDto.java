package com.wcacg.wcgal.entity.dto.favorite;

import com.wcacg.wcgal.entity.dto.user.UserInfoDto;

import java.util.Date;

public class FavoriteDto {
    private Long favoriteId;
    private String favoriteName;
    private String favoriteDescribe;
    private Boolean favoritePublic;
    private Integer size;
    private String cover;
    private Boolean inLike;
    private UserInfoDto createUser;
    protected Date createTime;
    protected Date updateTime;

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public String getFavoriteDescribe() {
        return favoriteDescribe;
    }

    public void setFavoriteDescribe(String favoriteDescribe) {
        this.favoriteDescribe = favoriteDescribe;
    }

    public Boolean getFavoritePublic() {
        return favoritePublic;
    }

    public void setFavoritePublic(Boolean favoritePublic) {
        this.favoritePublic = favoritePublic;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getInLike() {
        return inLike;
    }

    public void setInLike(Boolean inLike) {
        this.inLike = inLike;
    }

    public UserInfoDto getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserInfoDto createUser) {
        this.createUser = createUser;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
