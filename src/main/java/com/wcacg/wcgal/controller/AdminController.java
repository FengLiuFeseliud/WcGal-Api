package com.wcacg.wcgal.controller;


import com.wcacg.wcgal.entity.dto.user.UserTokenDto;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import com.wcacg.wcgal.exception.ClientError;
import com.wcacg.wcgal.service.AdminService;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping("/init")
    public ResponseMessage<Boolean> adminInit(HttpServletRequest request){
        UserTokenDto token = TokenUtils.decodedTokenOrNull(request);
        if (token != null && token.isAdmin()){
            return ResponseMessage.success(null);
        }

        if (!this.adminService.adminInit()){
            throw new ClientError.NotPermissionsException("你没有权限...");
        }
        return ResponseMessage.success(true);
    }
}
