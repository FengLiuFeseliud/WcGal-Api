package com.wcacg.wcgal.entity.dto.article;

import com.wcacg.wcgal.entity.dto.ArticleTagDto;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import lombok.Data;

import java.util.Date;

@Data
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
}
