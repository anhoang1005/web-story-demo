package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.config.Constant;
import com.example.webstorydemo.entity.Roles;
import com.example.webstorydemo.entity.Users;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.exceptions.users.AccountLockedException;
import com.example.webstorydemo.exceptions.users.InvalidCredentialsException;
import com.example.webstorydemo.mapper.users.UserMapper;
import com.example.webstorydemo.model.payload.JwtData;
import com.example.webstorydemo.model.payload.PageData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.user.UserDto;
import com.example.webstorydemo.model.user.UserRegisterRequest;
import com.example.webstorydemo.repository.RoleRepository;
import com.example.webstorydemo.repository.UserRepository;
import com.example.webstorydemo.services.FileService;
import com.example.webstorydemo.services.MailService;
import com.example.webstorydemo.services.UserService;
import com.example.webstorydemo.utils.AuthUtils;
import com.example.webstorydemo.utils.JwtTokenUtils;
import com.example.webstorydemo.utils.RandomUtils;
import com.example.webstorydemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IUserService implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final MailService mailService;
    private final FileService fileService;
    private final AuthUtils authUtils;

    @Override
    public JwtData loginUsers(String username, String password) {
        Users users = userRepository.findUsersByUsernameOrEmail(username, username)
                .orElseThrow(()-> new UsernameNotFoundException("Users not found!"));
        if (users != null && passwordEncoder.matches(password, users.getHashPassword())) {
            if (users.getStatus() == Users.Status.NORMAL) {
                List<String> listRoleString = users.getRolesList().stream()
                        .map(roles -> roles.getRoleName().name()).collect(Collectors.toList());
                String accessJws = jwtTokenUtils.generateAccessTokens(
                        users.getUsername(), listRoleString, 300);
                String refreshJws = jwtTokenUtils.generateRefreshTokens(
                        users.getUsername(), listRoleString, 300);
                return JwtData.builder()
                        .tokenType("Bearer ")
                        .accessToken(accessJws)
                        .refreshToken(refreshJws)
                        .dob(TimeMapperUtils.localDateToString(users.getDob()))
                        .username(users.getUsername())
                        .avatar(users.getAvatar())
                        .email(users.getEmail())
                        .issuedAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusDays(3))
                        .role(listRoleString)
                        .build();
            } else if (users.getStatus() == Users.Status.BLOCK) {
                log.info("Account Locked!");
                throw new AccountLockedException("Account Locked!");
            } else if (users.getStatus() == Users.Status.VERIFY) {
                log.info("Invalid Account!");
                throw new InvalidCredentialsException("Invalid Account");
            }
        } else {
            log.info("Invalid Account!");
            throw new InvalidCredentialsException("Invalid Account!");
        }
        return null;
    }

    private void createUser(UserRegisterRequest user){
        Roles role = roleRepository.findRolesByRoleName(Roles.BaseRole.USER)
                .orElseThrow(() -> new RequestNotFoundException("ERROR"));
        List<Roles> roleList = new ArrayList<>();
        roleList.add(role);
        Users users = new Users();
        String verifyCode = RandomUtils.verifyCode();
        users.setDob(TimeMapperUtils.stringToLocalDate(user.getDob()));
        users.setUsername(user.getUsername());
        users.setEmail(user.getEmail());
        users.setRolesList(roleList);
        users.setAvatar(Constant.USER_IMAGE);
        users.setStatus(Users.Status.VERIFY);
        users.setVerifyCode(passwordEncoder.encode(verifyCode));
        users.setHashPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(users);
        mailService.sendVerifyRegisterEmail(user.getEmail(), verifyCode);
    }

    @Override
    @Transactional
    public ResponseBody<?> registerUsers(UserRegisterRequest request) {
        try {
            Users checkUsername = userRepository.findUsersByUsername(request.getUsername())
                    .orElse(null);
            if(checkUsername!=null){
                return new ResponseBody<>(null, ResponseBody.Status.SUCCESS,
                        "USERNAME_EXISTED", ResponseBody.Code.SUCCESS);
            }

            Users checkEmail = userRepository.findUsersByEmail(request.getEmail())
                    .orElse(null);
            if(checkEmail!=null && checkEmail.getStatus()!= Users.Status.VERIFY){
                return new ResponseBody<>(null, ResponseBody.Status.SUCCESS,
                        "EMAIL_EXISTED", ResponseBody.Code.SUCCESS);
            } else if (checkEmail !=null && checkEmail.getStatus()== Users.Status.VERIFY) {
                if(Duration.between(checkEmail.getCreatedAt(), LocalDateTime.now()).toMinutes() > 15){
                    createUser(request);
                } else{
                    return new ResponseBody<>(checkEmail.getCreatedAt().plusMinutes(5).toString(),
                            ResponseBody.Status.SUCCESS, "REGISTER_LATER", ResponseBody.Code.SUCCESS);
                }
            } else {
                createUser(request);
            }
            return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Register account failed " + e.getMessage());
            throw new RequestNotFoundException("Register Failed!");
        }
    }

    @Override
    public ResponseBody<?> checkVerifyCodeRegister(String email, String verifyCode) {
        Users users = userRepository.findUsersByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        if (passwordEncoder.matches(verifyCode, users.getVerifyCode())) {
            users.setStatus(Users.Status.NORMAL);
            users.setVerifyCode(null);
            userRepository.save(users);
            return new ResponseBody<>("OK");
        } else {
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS,
                    "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    public ResponseBody<?> userForgotPassword(String email) {
        try {
            Users users = userRepository.findUsersByEmail(email)
                    .orElse(null);
            if(users==null){
                return new ResponseBody<>("", ResponseBody.Status.SUCCESS,
                        "USER_NOT_EXISTED", ResponseBody.Code.SUCCESS);
            } else{
                String verifyCode = RandomUtils.verifyCode();
                users.setVerifyCode(passwordEncoder.encode(verifyCode));
                users = userRepository.save(users);
                mailService.sendFogotPasswordMail(
                        users.getUsername(), email, verifyCode);
                return new ResponseBody<>("OK");
            }
        } catch (Exception e) {
            log.warn("Forget Password Failed");
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public ResponseBody<?> checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode) {
        Users users = userRepository.findUsersByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        if (passwordEncoder.matches(verifyCode, users.getVerifyCode())) {
            users.setHashPassword(passwordEncoder.encode(newPassword));
            users.setVerifyCode(null);
            userRepository.save(users);
            return new ResponseBody<>("OK");
        } else {
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS,
                    "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    public ResponseBody<?> changeUserStatus(Long userId, Users.Status status) {
        try {
            Users users = userRepository.findUsersById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("user not found!"));
            if (users.getStatus()!=status) {
                users.setStatus(status);
                userRepository.save(users);
            }
            return new ResponseBody<>("OK");
        } catch (Exception e){
            log.error("Change status error!");
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseBody<?> adminChangeRoleUsers(Long userId, boolean isAdmin) {
        Users users = userRepository.findUsersById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        try {
            List<Roles> userRoles = users.getRolesList();
            Roles admin = roleRepository.findRolesByRoleName(Roles.BaseRole.ADMIN)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            if (isAdmin) {
                if (!userRoles.contains(admin)) {
                    userRoles.add(admin);
                }
            } else {
                if (!userRoles.contains(admin)) {
                    userRoles.remove(admin);
                }
            }
            userRepository.save(users);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.error("Change roles users {} failed", users.getEmail());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody<?> adminGetPageUsers(String username, String email, Roles.BaseRole role, Users.Status status, String sort, int page, int size) {
        try{
            Roles roles = null;
            if(role!=null){
                roles = roleRepository.findRolesByRoleName(role).orElseThrow(()-> new RequestNotFoundException("role not found!"));
            }
            Pageable pageable;
            if(sort!=null){
                Sort sortValue = switch (sort) {
                    case "id_desc" -> Sort.by(Sort.Order.desc("id"));
                    case "id_asc" -> Sort.by(Sort.Order.asc("id"));
                    case "username_desc" -> Sort.by(Sort.Order.desc("username"));
                    case "username_asc" -> Sort.by(Sort.Order.asc("username"));
                    default -> Sort.by(Sort.Order.desc("createdAt"));
                };
                pageable = PageRequest.of(page - 1, size, sortValue);
            } else{
                pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
            }
            Page<Users> usersPage = userRepository.adminGetPageUser(username, email, roles, status, pageable);
            List<UserDto> userList = usersPage.stream()
                    .map(UserMapper::entityToUserDto)
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(userList)
                    .pageSize(size)
                    .pageNumber(page)
                    .totalPage(usersPage.getTotalPages())
                    .totalData(usersPage.getTotalElements())
                    .build();
            return new ResponseBody<>(pageData);
        } catch (Exception e){
            log.error("Get users failed");
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody<?> usersChangePassword(String oldPassword, String newPassword) {
        Long userId = authUtils.getUserFromAuthentication().getId();
        Users users = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RequestNotFoundException("User not found!"));
        if(passwordEncoder.matches(oldPassword, users.getHashPassword())){
            users.setHashPassword(passwordEncoder.encode(newPassword));
            userRepository.save(users);
            return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } else{
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS,
                    "INCORRECT_PASSWORD", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    public ResponseBody<?> usersChangeUserInfo(UserRegisterRequest req) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users user = userRepository.findUsersById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found!"));
            if(req.getUsername().equals(user.getUsername())){
                user.setDob(TimeMapperUtils.stringToLocalDate(req.getDob()));
                userRepository.save(user);
                return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else{
                Users checkUsername = userRepository.findUsersByUsername(req.getUsername())
                        .orElse(null);
                if(checkUsername!=null){
                    return new ResponseBody<>("", ResponseBody.Status.SUCCESS,
                            "USERNAME_EXISTED", ResponseBody.Code.SUCCESS);
                } else{
                    user.setUsername(req.getUsername());
                }
                user.setDob(TimeMapperUtils.stringToLocalDate(req.getDob()));
                userRepository.save(user);
                return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }

        } catch (Exception e){
            log.error("Change info error! Error " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody<?> usersChangeAvatar(MultipartFile file) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users = userRepository.findUsersById(userId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            String url = fileService.uploadToCloudinary(file);
            users.setAvatar(url);
            users = userRepository.save(users);
            return new ResponseBody<>(
                    UserMapper.entityToUserDto(users),
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS
            );
        } catch (Exception e){
            log.error("Get info error! Error " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody<?> usersGetUsersDetail() {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users = userRepository.findUsersById(userId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            return new ResponseBody<>(
                    UserMapper.entityToUserDto(users),
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS
            );
        } catch (Exception e){
            log.error("Get info error! Error " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
