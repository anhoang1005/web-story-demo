package com.example.webstorydemo.mapper.users;

import com.example.webstorydemo.entity.Users;
import com.example.webstorydemo.model.user.UserDto;
import com.example.webstorydemo.utils.TimeMapperUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto entityToUserDto(Users users){
        List<String> roleList = users.getRolesList().stream()
                .map(roles -> roles.getRoleName().toString()).collect(Collectors.toList());
        return UserDto.builder()
                .id(users.getId())
                .username(users.getUsername())
                .avatar(users.getAvatar())
                .dob(TimeMapperUtils.localDateToString(users.getDob()))
                .email(users.getEmail())
                .roles(roleList)
                .status(users.getStatus())
                .createdAt(TimeMapperUtils.localDateTimeToString(users.getCreatedAt()))
                .updatedAt(TimeMapperUtils.localDateTimeToString(users.getUpdatedAt()))
                .build();
    }
}
