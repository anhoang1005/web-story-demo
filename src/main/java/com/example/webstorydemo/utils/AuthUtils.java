package com.example.webstorydemo.utils;

import com.example.webstorydemo.config.security.user_detail.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthUtils {

	public CustomUserDetail getUserFromAuthentication(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !authentication.getPrincipal().equals("anonymousUser")) {
			return (CustomUserDetail) authentication.getPrincipal();
		}
		return null;
	}

	public List<String> getRoleFromAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getAuthorities() == null ||
				authentication.getPrincipal().equals("anonymousUser")) {
			return null;
		}
		return authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
	}
}
