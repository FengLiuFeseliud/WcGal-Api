package com.wcacg.wcgal.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private static final String tokenKey = "token";

    public static String getToken(int expiresMinute, UserDto user) {
        return JWT.create()
                .withClaim("user_id", user.getUserId())
                .withClaim("user_name", user.getUserName())
                .withClaim("admin", user.isAdmin())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * expiresMinute))
                .sign(Algorithm.HMAC256(tokenKey));
    }

    public static Map<String, String> decodedToken(String token) throws JWTVerificationException {
        Map<String, String> map = new HashMap<>();
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(tokenKey)).build().verify(token);
        map.put("user_id", jwt.getClaim("user_id").toString());
        map.put("user_name", jwt.getClaim("user_name").asString());
        map.put("admin", jwt.getClaim("admin").toString());
        map.put("exp", jwt.getClaim("exp").toString());
        return map;
    }

    public static Map<String, String> decodedToken(HttpServletRequest request) throws JWTVerificationException {
        return TokenUtils.decodedToken(request.getHeader("token"));
    }

    public static long decodedTokenUserId(HttpServletRequest request) throws JWTVerificationException {
        String token = request.getHeader("token");
        if (token == null) {
            return 0;
        }

        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(tokenKey)).build().verify(token);
        return jwt.getClaim("user_id").asLong();
    }

    public static long decodedTokenUserIdOrNotUserId(HttpServletRequest request){
        try{
            return decodedTokenUserId(request);
        } catch (JWTVerificationException exception){
            return 0;
        }
    }
}
