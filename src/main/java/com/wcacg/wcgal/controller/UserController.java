package com.wcacg.wcgal.controller;

import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.entity.dto.*;

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

import java.util.List;

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

    @PostMapping("/login")
    public ResponseMessage<UserDto> login(@Validated @RequestBody UserLoginDto userLoginDto) {
        UserDto user = this.userService.login(userLoginDto);
        if (user == null) {
            return ResponseMessage.dataError("登录失败... qwq", null);
        }

        user.setToken(TokenUtils.getToken(7, user));
        return ResponseMessage.success(user);
    }

    @PostMapping("/register")
    public ResponseMessage<UserDto> register(@Validated @RequestBody UserRegisterDto userRegisterDto, HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("code") == null){
            return ResponseMessage.dataError("还没有获取邮箱验证码... qwq", null);
        }

        if (!session.getAttribute("code").toString().equals(userRegisterDto.getCode() + userRegisterDto.getEmail())){
            return ResponseMessage.dataError("邮箱验证码错误... qwq", null);
        }

        session.removeAttribute("code");
        switch (this.userService.canRegister(userRegisterDto)){
            case EmailError -> {
                return ResponseMessage.dataError("邮箱已被注册了... qwq", null);
            }

            case UserNameError -> {
                return ResponseMessage.dataError("用户名已被注册了... qwq", null);
            }
        }
        return ResponseMessage.success(this.userService.register(userRegisterDto));
    }

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
}
