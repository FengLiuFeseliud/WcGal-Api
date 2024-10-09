package com.wcacg.wcgal.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcacg.wcgal.annotation.NeedAdmin;
import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.message.LoginMessage;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    private ObjectMapper mapper = new ObjectMapper();

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
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(mapper.writeValueAsString(LoginMessage.error()));
            return false;
        }

        Map<String, String> tokenData;
        try {
            tokenData = TokenUtils.decodedToken(token);
        } catch (Exception e) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(mapper.writeValueAsString(LoginMessage.error()));
            return false;
        }

        NeedAdmin needAdmin = handlerMethod.getMethodAnnotation(NeedAdmin.class);
        if (needAdmin == null) {
            return true;
        }

        if (!tokenData.get("admin").equals("true")){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(mapper.writeValueAsString(LoginMessage.errorAdmin()));
            return false;
        }

        return true;
    }

}
