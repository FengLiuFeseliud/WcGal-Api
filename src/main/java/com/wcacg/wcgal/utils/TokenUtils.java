package com.wcacg.wcgal.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import com.wcacg.wcgal.entity.dto.user.UserTokenDto;
import com.wcacg.wcgal.exception.ClientError;
import jakarta.servlet.http.HttpServletRequest;
import org.paseto4j.commons.PasetoException;
import org.paseto4j.commons.SecretKey;
import org.paseto4j.commons.Version;
import org.paseto4j.version4.Paseto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenUtils {
    private static final String secret = "WfvKvfSqJRKkGRe54NvNyH9M4HAyHNwd";
    private static final String footer = "WC-GALGAME-TOKEN";

    public static String getToken(UserDto user) {
        JsonMapper mapper = new JsonMapper();
        mapper.registerModule(new JavaTimeModule());

        UserTokenDto userTokenDto = new UserTokenDto();
        BeanUtils.copyProperties(user, userTokenDto);
        userTokenDto.setExpiresDate(new Date(System.currentTimeMillis() + 1000L * 60 * 120));
        try {
            return Paseto.encrypt(new SecretKey(secret.getBytes(StandardCharsets.UTF_8), Version.V4),
                    mapper.writeValueAsString(userTokenDto), footer);
        } catch (PasetoException | JsonProcessingException e) {
            throw new ClientError.NotTokenException("token 创建失败");
        }
    }

    public static UserTokenDto decodedToken(String token) {
        JsonMapper mapper = new JsonMapper();
        mapper.registerModule(new JavaTimeModule());

        UserTokenDto userTokenDto = null;
        try {
            userTokenDto = mapper.readValue(Paseto.decrypt(new SecretKey(
                    secret.getBytes(StandardCharsets.UTF_8), Version.V4), token, footer), UserTokenDto.class);
        } catch (PasetoException | JsonProcessingException e) {
            throw new ClientError.NotTokenException("你还没未登录呢 ...");
        }

        if (new Date(System.currentTimeMillis()).getTime() > userTokenDto.getExpiresDate().getTime()){
            throw new ClientError.NotTokenException("你还没未登录呢 ...");
        }
        return userTokenDto;
    }

    public static UserTokenDto decodedToken(HttpServletRequest request){
        String token = request.getHeader("token");
        if (token == null) {
            throw new ClientError.NotTokenException("你还没未登录呢 ...");
        }
        return TokenUtils.decodedToken(token);
    }

    public static long decodedTokenUserId(HttpServletRequest request){
        return decodedToken(request).getUserId();
    }

    public static long decodedTokenUserIdOrNotUserId(HttpServletRequest request){
        try{
            return decodedTokenUserId(request);
        } catch (ClientError.NotTokenException exception){
            return 0;
        }
    }

    public static boolean canResetToken(UserTokenDto userTokenDto){
        return TimeUtils.isEffectiveDate(new Date(System.currentTimeMillis()),
                new Date(userTokenDto.getExpiresDate().getTime() - 1000L * 50), userTokenDto.getExpiresDate());
    }
}
