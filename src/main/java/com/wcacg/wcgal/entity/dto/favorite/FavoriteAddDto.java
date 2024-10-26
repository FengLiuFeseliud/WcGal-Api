package com.wcacg.wcgal.entity.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FavoriteAddDto {
    @NotBlank
    private String favoriteName;
    private String favoriteDescribe;
    @NotNull
    private Boolean favoritePublic;
    private String cover;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
