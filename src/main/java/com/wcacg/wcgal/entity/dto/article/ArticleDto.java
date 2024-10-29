package com.wcacg.wcgal.entity.dto.article;

import com.wcacg.wcgal.entity.dto.ArticleTagDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteItemDto;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import lombok.Data;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.Date;
import java.util.List;

@Data
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
    private FavoriteItemDto likeItem;
    private List<FavoriteItemDto> favoriteItems;

    public String getTags() {
        return StringUtils.join(tags);
    }
}
