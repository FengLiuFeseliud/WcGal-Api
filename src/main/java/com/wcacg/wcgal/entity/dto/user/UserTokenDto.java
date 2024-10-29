package com.wcacg.wcgal.entity.dto.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserTokenDto {
    private long userId;
    private String userName;
    private boolean admin;
    private Date expiresDate;
}
