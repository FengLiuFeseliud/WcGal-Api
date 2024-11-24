package com.wcacg.wcgal.service;

import com.wcacg.wcgal.entity.Config;
import com.wcacg.wcgal.entity.QConfig;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.repository.ConfigRepository;
import com.wcacg.wcgal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private static final String INIT_ADMIN_CONFIG_NAME = "initAdmin";

    @Value("${app.init.admin.username:admin}")
    private String adminInitUserName;

    @Value("${app.init.admin.email:admin@admin.com}")
    private String adminInitEmail;

    @Value("${app.init.admin.password:admin123456789}")
    private String adminInitPassword;

    private final ConfigRepository configRepository;
    private final UserRepository userRepository;

    public AdminService(ConfigRepository configRepository, UserRepository userRepository){
        this.configRepository = configRepository;
        this.userRepository = userRepository;
    }

    public boolean adminInit(){
        QConfig config = QConfig.config;
        if (this.configRepository.findOne(config.configName.eq(INIT_ADMIN_CONFIG_NAME).and(config.configDate.eq("true"))).isPresent()){
            return false;
        }

        this.userRepository.save(new User(this.adminInitUserName, this.adminInitEmail, this.adminInitPassword, true));
        this.configRepository.save(new Config(INIT_ADMIN_CONFIG_NAME, "true"));
        return true;
    }
}
