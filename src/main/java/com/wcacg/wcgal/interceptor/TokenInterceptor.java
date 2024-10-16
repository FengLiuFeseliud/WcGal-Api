package com.wcacg.wcgal.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcacg.wcgal.annotation.NeedAdmin;
import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import com.wcacg.wcgal.entity.message.LoginMessage;
import com.wcacg.wcgal.repository.UserRepository;
import com.wcacg.wcgal.utils.TimeUtils;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Date;
import java.util.Map;


/**
 * 表示接口需要 token @NeedToken
 * 表示接口需要 admin @NeedAdmin
 * token 续签时存于响应头中
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    public TokenInterceptor(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private boolean TokenError(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(this.mapper.writeValueAsString(LoginMessage.error()));
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        NeedToken needToken = handlerMethod.getMethodAnnotation(NeedToken.class);
        if (needToken == null) {
            return true;
        }

        String token = request.getHeader("token");
        if (token == null) {
            // 没有 token
            return this.TokenError(response);
        }

        Map<String, String> tokenData;
        try {
            tokenData = TokenUtils.decodedToken(token);
        } catch (Exception e) {
            // token 验证失败
            return this.TokenError(response);
        }

        long exp = Long.parseLong(tokenData.get("exp"));
        if (TimeUtils.isEffectiveDate(new Date(System.currentTimeMillis() / 1000), new Date(exp - 30), new Date(exp))) {
            // token 续签
            User user = userRepository.findById(Long.valueOf(tokenData.get("user_id"))).orElse(null);
            if (user == null){
                // token 验证失败
                return this.TokenError(response);
            }

            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            token = TokenUtils.getToken(120, userDto);
            response.setHeader("Access-Control-Expose-Headers", "token");
            response.setHeader("token", token);
        }

        // admin 验证
        NeedAdmin needAdmin = handlerMethod.getMethodAnnotation(NeedAdmin.class);
        if (needAdmin == null) {
            return true;
        }

        if (!tokenData.get("admin").equals("true")){
            // 不是 admin
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(mapper.writeValueAsString(LoginMessage.errorAdmin()));
            return false;
        }

        return true;
    }

}
