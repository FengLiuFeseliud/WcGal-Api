package com.wcacg.wcgal.entity.dto.favorite;

import com.wcacg.wcgal.entity.dto.article.ArticleInfoDto;
import lombok.Data;

import java.util.Date;

@Data
public class FavoriteItemDto {
    private Long favoriteId;
    private Long favoriteUserId;
    private String resourceId;
    private ArticleInfoDto article;
    protected Date createTime;
}
