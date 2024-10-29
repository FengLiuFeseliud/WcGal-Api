package com.wcacg.wcgal.entity.dto.user;

import lombok.Data;

/**
 * 用户基本信息
 */
@Data
public class UserInfoDto {
    private long userId;
    private String userName;
    private String head;
    private boolean admin;
}
