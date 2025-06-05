package com.example.webstorydemo.controller;

import com.example.webstorydemo.entity.Roles;
import com.example.webstorydemo.entity.Users;
import com.example.webstorydemo.model.payload.JwtData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.user.UserRegisterRequest;
import com.example.webstorydemo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/guest/users/login")
    public ResponseEntity<?> loginUsersApi(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        ResponseBody<JwtData> responseBody = new ResponseBody<>(
                userService.loginUsers(username, password),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/api/guest/users/register")
    public ResponseEntity<?> registerUserApi(
            @RequestBody UserRegisterRequest user) {
        return new ResponseEntity<>(userService.registerUsers(user), HttpStatus.OK);
    }

    @GetMapping("/api/guest/users/check-register")
    public ResponseEntity<?> checkRegisterApi(
            @RequestParam("email") String email,
            @RequestParam("verify_code") String verifyCode
    ) {
        return new ResponseEntity<>(userService.checkVerifyCodeRegister(email, verifyCode), HttpStatus.OK);
    }

    @GetMapping("/api/guest/users/forgot-password")
    public ResponseEntity<?> forgotPasswordApi(
            @RequestParam("email") String email
    ) {
        return new ResponseEntity<>(userService.userForgotPassword(email), HttpStatus.OK);
    }

    @PutMapping("/api/guest/users/check-forgot")
    public ResponseEntity<?> checkVerifyForgotPasswordApi(
            @RequestParam("email") String email,
            @RequestParam("new_password") String newPassword,
            @RequestParam("verify_code") String verifyCode
    ) {
        return new ResponseEntity<>(userService.checkVerifyCodeForgotPassword(email, newPassword,
                verifyCode), HttpStatus.OK);
    }

    @GetMapping("/api/user/users/detail")
    public ResponseEntity<?> userGetUserDetailApi() {
        return ResponseEntity.ok(userService.usersGetUsersDetail());
    }

    @PutMapping("/api/user/users/change-password")
    public ResponseEntity<?> userChangePasswordApi(
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword
    ) {
        return ResponseEntity.ok(userService.usersChangePassword(oldPassword, newPassword));
    }

    @PutMapping("/api/user/users/change-info")
    public ResponseEntity<?> userChangeInfoApi(
            @RequestBody UserRegisterRequest userInfo) {
        return ResponseEntity.ok(userService.usersChangeUserInfo(userInfo));
    }

    @PutMapping("/api/user/users/change-avatar")
    public ResponseEntity<?> userChangeAvatarApi(
            @RequestBody MultipartFile file) {
        return ResponseEntity.ok(userService.usersChangeAvatar(file));
    }

    @PutMapping("/api/root/users/change-admin")
    public ResponseEntity<?> rootChangeAdminRoleApi(
            @RequestParam("user_code") Long userId,
            @RequestParam("is_admin") boolean isAdmin) {
        return ResponseEntity.ok(userService.adminChangeRoleUsers(userId, isAdmin));
    }

    @GetMapping("/api/admin/users")
    public ResponseEntity<?> adminGetPageUser(@RequestParam(value = "username", required = false) String username,
                                              @RequestParam(value = "email", required = false) String email,
                                              @RequestParam(value = "role", required = false) Roles.BaseRole role,
                                              @RequestParam(value = "status", required = false) Users.Status status,
                                              @RequestParam(value = "sort", required = false) String sort,
                                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "size", required = false, defaultValue = "20") int size){
        return ResponseEntity.ok(userService.adminGetPageUsers(username, email, role, status, sort, page, size));
    }

    @PutMapping("/api/admin/users/status")
    public ResponseEntity<?> adminChangeStatus(@RequestParam("user_id") Long userId,
                                               @RequestParam("status") Users.Status status){
        return ResponseEntity.ok(userService.changeUserStatus(userId, status));
    }
}
