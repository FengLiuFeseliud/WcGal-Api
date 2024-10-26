package com.wcacg.wcgal.entity.dto.favorite;

import com.wcacg.wcgal.entity.FavoriteItem;

import java.util.List;

public class FavoriteAndItemsDto extends FavoriteDto{
    private List<FavoriteItem> favoriteItems;

    public List<FavoriteItem> getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(List<FavoriteItem> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }
}
