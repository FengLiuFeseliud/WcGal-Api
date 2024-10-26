package com.wcacg.wcgal.controller;

import com.wcacg.wcgal.annotation.NeedAdmin;
import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.entity.dto.EmailDto;
import com.wcacg.wcgal.entity.dto.user.ResetPasswordDto;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import com.wcacg.wcgal.entity.dto.user.UserLoginDto;
import com.wcacg.wcgal.entity.dto.user.UserRegisterDto;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import com.wcacg.wcgal.service.UserService;
import com.wcacg.wcgal.utils.TokenUtils;
import com.wcacg.wcgal.utils.VerCodeGenerateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final String EMAIL;
    private final JavaMailSender mailSender;
    private final UserService userService;

    public UserController(@Value("${spring.mail.username}") String email, UserService userService, JavaMailSender mailSender) {
        EMAIL = email;
        this.userService = userService;
        this.mailSender = mailSender;
    }

    /**
     * 获取用户
     * @param userId 用户 id
     * @return 用户信息
     */
    @PostMapping("/{userId}")
    public ResponseMessage<UserDto> getUser(@PathVariable Long userId, HttpServletRequest request){
        long tokenUserId = TokenUtils.decodedTokenUserIdOrNotUserId(request);
        UserDto userDto = this.userService.getUser(userId);
        if (tokenUserId != userDto.getUserId()){
            userDto.setEmail(null);
        }
        return ResponseMessage.success(userDto);
    }

    /**
     * 发送验证码
     * @param email 发送到邮箱
     * @param session 验证码保存至 session 会话
     * @return 响应状态消息
     */
    @PostMapping("/code")
    public ResponseMessage<Null> code(@Validated @RequestBody EmailDto email, HttpSession session) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL);
        message.setTo(email.getEmail());

        String code = VerCodeGenerateUtils.generateVerCode();
        message.setText("验证码: " + code + "\n有效时长 5 分钟");
        mailSender.send(message);

        session.setAttribute("code", code + email.getEmail());
        session.setMaxInactiveInterval(300);
        return ResponseMessage.success(null);
    }

    /**
     * 用户登录
     * @param userLoginDto 用户登录数据
     * @return 用户信息带 token
     */
    @PostMapping("/login")
    public ResponseMessage<UserDto> login(@Validated @RequestBody UserLoginDto userLoginDto) {
        UserDto user = this.userService.login(userLoginDto);
        if (user == null) {
            return ResponseMessage.dataError("登录失败... qwq", null);
        }

        user.setToken(TokenUtils.getToken(120, user));
        return ResponseMessage.success(user);
    }

    /**
     * 用户注册
     * @param userRegisterDto 用户注册数据
     * @param session 从 session 会话获取验证码
     * @param response 响应头
     * @return 用户信息
     */
    @PostMapping("/register")
    public ResponseMessage<UserDto> register(@Validated @RequestBody UserRegisterDto userRegisterDto, HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("code") == null){
            return ResponseMessage.dataError("还没有获取邮箱验证码... qwq", null);
        }

        if (!session.getAttribute("code").toString().equals(userRegisterDto.getCode() + userRegisterDto.getEmail())){
            return ResponseMessage.dataError("邮箱验证码错误... qwq", null);
        }

        session.removeAttribute("code");
        return ResponseMessage.success(this.userService.register(userRegisterDto));
    }

    /**
     * 获取 token 用户信息
     * @param request 请求头
     * @return 用户信息
     */
    @NeedToken
    @PostMapping("/info")
    public ResponseMessage<UserDto> resetToken(HttpServletRequest request, HttpServletResponse  response){
        Map<String, String> tokenData = TokenUtils.decodedToken(request);
        UserDto user = this.userService.getUser(Long.parseLong(tokenData.get("user_id")));
        if (user.isAdmin() != Boolean.parseBoolean(tokenData.get("admin"))){
            response.setHeader("Access-Control-Expose-Headers", "token");
            response.setHeader("token", TokenUtils.getToken(120, user));
        }
        user.setFavorites(null);
        return ResponseMessage.success(user);
    }

    /**
     * 重设密码
     * @param resetPasswordDto 重设密码数据
     * @param session 从 session 会话获取验证码
     * @return 响应状态消息
     */
    @PostMapping("/reset/password")
    public ResponseMessage<Null> resetPassword(@Validated @RequestBody ResetPasswordDto resetPasswordDto, HttpSession session) {
        if (session.getAttribute("code") == null){
            return ResponseMessage.dataError("还没有获取邮箱验证码... qwq", null);
        }

        if (!session.getAttribute("code").toString().equals(resetPasswordDto.getCode() + resetPasswordDto.getEmail())){
            return ResponseMessage.dataError("邮箱验证码错误... qwq", null);
        }

        session.removeAttribute("code");
        User user = this.userService.getUserFormEmail(resetPasswordDto.getEmail());
        if (user == null) {
            return ResponseMessage.dataError("这个邮箱还没被注册呢... qwq", null);
        }

        this.userService.resetPassword(user, resetPasswordDto.getPassword());
        return ResponseMessage.success(null);
    }

    /**
     * 设置用户为管理员
     * @param userId 用户id
     * @return 用户信息
     */
    @NeedAdmin
    @NeedToken
    @PostMapping("/admin/set/{userId}")
    public ResponseMessage<UserDto> setAdmin(@PathVariable Long userId){
        return ResponseMessage.success(this.userService.setAdmin(userId, true));
    }

    /**
     * 取消用户为管理员
     * @param userId 用户id
     * @return 用户信息
     */
    @NeedAdmin
    @NeedToken
    @PostMapping("/admin/undo/{userId}")
    public ResponseMessage<UserDto> undoAdmin(@PathVariable Long userId){
        return ResponseMessage.success(this.userService.setAdmin(userId, false));
    }
}
