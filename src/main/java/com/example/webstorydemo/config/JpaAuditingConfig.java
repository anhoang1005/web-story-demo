package com.example.webstorydemo.config;

import com.example.webstorydemo.config.security.user_detail.CustomUserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
	
	@Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

	// Config de lay createdBy va ModifyBy trong Entity
    public static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
            	return Optional.of("guest");
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetail userDetails) {
                return Optional.ofNullable(userDetails.getEmail());
            }
            return Optional.of("guest");
        }
    }
}