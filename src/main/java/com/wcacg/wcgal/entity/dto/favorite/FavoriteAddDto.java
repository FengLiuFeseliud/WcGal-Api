package com.wcacg.wcgal.entity.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public class FavoriteAddDto {

    @Length(min = 1, max = 50)
    @NotBlank
    private String favoriteName;

    @Length(min = 1, max = 500)
    private String favoriteDescribe;
    @NotNull
    private Boolean favoritePublic;

    @URL
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
