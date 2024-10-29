package com.wcacg.wcgal.entity.dto.user;

import com.wcacg.wcgal.entity.dto.favorite.FavoriteDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户详细信息
 */
@Data
public class UserDto {
    private long userId;
    private String userName;
    private String email;
    private String head;
    private boolean admin;
    private List<FavoriteDto> favorites = new ArrayList<>();
    private Date createTime;
    private Date updateTime;
    private String token;
}
