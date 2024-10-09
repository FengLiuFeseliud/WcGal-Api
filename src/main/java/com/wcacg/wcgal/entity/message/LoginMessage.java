package com.wcacg.wcgal.entity.message;

import com.wcacg.wcgal.entity.dto.user.UserDto;
import org.springframework.http.HttpStatus;

public record LoginMessage(int code, String message, UserDto data) implements IMessage<UserDto>{

    public static LoginMessage error() {
        return new LoginMessage(HttpStatus.UNAUTHORIZED.value(), "你还没未登录呢 ...", null);
    }
    public static LoginMessage errorAdmin() {
        return new LoginMessage(HttpStatus.FORBIDDEN.value(), "你没有权限...", null);
    }


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public UserDto getData() {
        return data;
    }
}
