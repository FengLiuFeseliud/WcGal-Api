package com.wcacg.wcgal.entity.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class FavoriteSetItemDto {
    @NotNull
    private List<Long> favoriteIds;

    @NotBlank
    private String resourceId;

    public List<Long> getFavoriteIds() {
        return favoriteIds;
    }

    public void setFavoriteIds(List<Long> favoriteIds) {
        this.favoriteIds = favoriteIds;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
