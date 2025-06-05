package com.example.webstorydemo.services;

import com.example.webstorydemo.entity.Roles;
import com.example.webstorydemo.entity.Users;
import com.example.webstorydemo.model.payload.JwtData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.user.UserRegisterRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    JwtData loginUsers(String username, String password);
    ResponseBody<?> registerUsers(UserRegisterRequest user);
    ResponseBody<?> checkVerifyCodeRegister(String email, String verifyCode);
    ResponseBody<?> userForgotPassword(String email);
    ResponseBody<?> checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode);

    ResponseBody<?> changeUserStatus(Long userId, Users.Status status);
    ResponseBody<?> adminChangeRoleUsers(Long userId, boolean isAdmin);
    ResponseBody<?> adminGetPageUsers(String username, String email, Roles.BaseRole role,
                                      Users.Status status, String sort, int page, int size);

    ResponseBody<?> usersChangePassword(String oldPassword, String newPassword);
    ResponseBody<?> usersChangeUserInfo(UserRegisterRequest req);
    ResponseBody<?> usersChangeAvatar(MultipartFile file);
    ResponseBody<?> usersGetUsersDetail();

}
