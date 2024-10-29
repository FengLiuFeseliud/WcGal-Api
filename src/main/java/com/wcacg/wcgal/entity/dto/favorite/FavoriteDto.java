package com.wcacg.wcgal.entity.dto.favorite;

import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import lombok.Data;

import java.util.Date;

@Data
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
}
