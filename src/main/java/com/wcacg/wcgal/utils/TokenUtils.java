package com.wcacg.wcgal.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private static final String tokenKey = "token";

    public static String getToken(int expiresDay, UserDto user) {
        return JWT.create()
                .withClaim("user_id", user.getUserId())
                .withClaim("admin", user.isAdmin())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * expiresDay))
                .sign(Algorithm.HMAC256(tokenKey));
    }

    public static Map<String, String> decodedToken(String token) throws Exception {
        Map<String, String> map = new HashMap<>();
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(tokenKey)).build().verify(token);
        map.put("user_id", jwt.getClaim("user_id").toString());
        map.put("admin", jwt.getClaim("admin").toString());
        return map;
    }

    public static long decodedTokenUserId(HttpServletRequest request) throws JWTVerificationException {
        String token = request.getHeader("token");
        if (token == null) {
            return 0;
        }

        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(tokenKey)).build().verify(token);
        return jwt.getClaim("user_id").asLong();
    }
}
