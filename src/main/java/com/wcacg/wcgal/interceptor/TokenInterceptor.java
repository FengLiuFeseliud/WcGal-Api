package com.wcacg.wcgal.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcacg.wcgal.annotation.NeedAdmin;
import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import com.wcacg.wcgal.entity.dto.user.UserTokenDto;
import com.wcacg.wcgal.exception.ClientError;
import com.wcacg.wcgal.repository.UserRepository;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        NeedToken needToken = handlerMethod.getMethodAnnotation(NeedToken.class);
        if (needToken == null) {
            return true;
        }

        UserTokenDto userTokenDto = TokenUtils.decodedToken(request);
        if (TokenUtils.canResetToken(userTokenDto)) {
            // token 续签
            User user = userRepository.findById(userTokenDto.getUserId()).orElse(null);
            if (user == null){
                throw new ClientError.NotTokenException("你还没未登录呢 ...");
            }

            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            response.setHeader("Access-Control-Expose-Headers", "token");
            response.setHeader("token", TokenUtils.getToken(userDto));
        }

        // admin 验证
        NeedAdmin needAdmin = handlerMethod.getMethodAnnotation(NeedAdmin.class);
        if (needAdmin == null) {
            return true;
        }

        if (userTokenDto.isAdmin()){
            // 不是 admin
            throw new ClientError.NotPermissionsException("你没有权限...");
        }

        return true;
    }

}
