package com.example.webstorydemo.config.security.user_detail;

import com.example.webstorydemo.entity.Roles;
import com.example.webstorydemo.entity.Users;
import com.example.webstorydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository usersRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findUsersByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Users not found!"));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles role : users.getRolesList()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()));
        }
        return CustomUserDetail.builder()
                .id(users.getId())
                .username(users.getUsername())
                .email(users.getEmail())
                .status(users.getStatus())
                .password(users.getHashPassword())
                .authorities(authorities)
                .build();
    }
}
