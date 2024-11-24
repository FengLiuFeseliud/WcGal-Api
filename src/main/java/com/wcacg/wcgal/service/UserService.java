package com.wcacg.wcgal.service;

import com.wcacg.wcgal.entity.QUser;
import com.wcacg.wcgal.entity.User;
import com.wcacg.wcgal.entity.dto.user.UserDto;
import com.wcacg.wcgal.entity.dto.user.UserLoginDto;
import com.wcacg.wcgal.entity.dto.user.UserRegisterDto;
import com.wcacg.wcgal.exception.ClientError;
import com.wcacg.wcgal.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static String strMd5(String str, String salt) {
        return DigestUtils.md5Hex(DigestUtils.sha256(DigestUtils.md5Hex(DigestUtils.sha256(str + salt))));
    }

    private UserDto getUserDto(User user) {
        if (user == null){
            throw new ClientError.NotFindException("不存在的用户... qwq");
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        if (user.getFavorites() != null){
            user.getFavorites().forEach(favorite ->
                    userDto.getFavorites().add(FavoriteService.favoriteToFavoriteDto(favorite)));
        }
        return userDto;
    }

    public UserDto login(UserLoginDto userLoginDto) {
        User user = this.userRepository.findByEmail(userLoginDto.getEmail());
        if (user == null) {
            throw new ClientError.NotFindException("不存在的用户... qwq");
        }

        if (!Objects.equals(user.getPassword(), strMd5(userLoginDto.getPassword(), user.getSalt()))) {
            throw new ClientError.NotPermissionsException("密码错误... qwq");
        }

        user.setFavorites(null);
        return this.getUserDto(user);
    }

    public UserDto register(UserRegisterDto userRegisterDto) {
        QUser qUser = QUser.user;
        Iterable<User> users = this.userRepository.findAll(
                qUser.userName.eq(userRegisterDto.getUserName()).or(qUser.email.eq(userRegisterDto.getEmail())));

        for (User user : users) {
            if (user.getUserName().equals(userRegisterDto.getUserName())) {
                throw new ClientError.HaveExistedException("用户名已被注册了... qwq");
            }

            if (user.getEmail().equals(userRegisterDto.getEmail())) {
                throw new ClientError.HaveExistedException("邮箱已被注册了... qwq");
            }
        }

        User user = new User();
        String salt = UUID.randomUUID().toString();

        BeanUtils.copyProperties(userRegisterDto, user);
        user.setPassword(strMd5(userRegisterDto.getPassword(), salt));
        user.setSalt(salt);
        userRepository.save(user);
        user.setSalt("");
        return this.getUserDto(user);
    }

    public void resetPassword(User user, String password) {
        user.setPassword(strMd5(password, user.getSalt()));
        userRepository.save(user);
    }

    public UserDto getUser(long userId) {
        return this.getUserDto(this.userRepository.findById(userId).orElse(null));
    }

    public User getUserFill(long userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    public User getUserFormEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public UserDto setAdmin(long userId, boolean setIn){
        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ClientError.NotFindException("不存在的用户... qwq");
        }

        user.setAdmin(setIn);
        return this.getUserDto(this.userRepository.save(user));
    }
}
