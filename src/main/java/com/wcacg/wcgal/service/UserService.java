package com.wcacg.wcgal.service;

import com.wcacg.wcgal.entity.QUser;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import com.wcacg.wcgal.entity.dto.user.UserLoginDto;
import com.wcacg.wcgal.entity.dto.user.UserRegisterDto;
import com.wcacg.wcgal.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    public enum RegisterType{
        UserNameError,
        EmailError,
        Ok;
    }

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String strMd5(String str, String salt) {
        return DigestUtils.md5Hex(DigestUtils.sha256(str + salt));
    }

    private UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public UserDto login(UserLoginDto userLoginDto) {
        User user = this.userRepository.findByEmail(userLoginDto.getEmail());
        if (user == null) {
            return null;
        }

        if (!Objects.equals(user.getPassword(), this.strMd5(userLoginDto.getPassword(), user.getSalt()))) {
            return null;
        }
        return this.getUserDto(user);
    }

    public RegisterType canRegister(UserRegisterDto userRegisterDto) {
        QUser qUser = QUser.user;
        Iterable<User> users = this.userRepository.findAll(
                qUser.userName.eq(userRegisterDto.getUserName()).or(qUser.email.eq(userRegisterDto.getEmail())));

        for (User user : users) {
            if (user.getUserName().equals(userRegisterDto.getUserName())) {
                return RegisterType.UserNameError;
            }

            if (user.getEmail().equals(userRegisterDto.getEmail())) {
                return RegisterType.EmailError;
            }
        }

        return RegisterType.Ok;
    }

    public UserDto register(UserRegisterDto userRegisterDto) {
        User user = new User();
        String salt = UUID.randomUUID().toString();

        BeanUtils.copyProperties(userRegisterDto, user);
        user.setPassword(this.strMd5(userRegisterDto.getPassword(), salt));
        user.setSalt(salt);
        userRepository.save(user);
        user.setSalt("");
        return this.getUserDto(user);
    }

    public void resetPassword(User user, String password) {
        user.setPassword(this.strMd5(password, user.getSalt()));
        userRepository.save(user);
    }

    public UserDto getUser(UserRegisterDto userRegisterDto) {
        return this.getUserDto(this.userRepository.findByUserName(userRegisterDto.getUserName()));
    }

    public User getUser(long userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    public User getUserFormEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
