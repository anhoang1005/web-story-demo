package com.example.webstorydemo.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtData {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType = "Bearer";
    @JsonProperty("username")
    private String username;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("dob")
    private String dob;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private List<String> role;
    @JsonProperty("issued_at")
    private LocalDateTime issuedAt;
    @JsonProperty("expired_at")
    private LocalDateTime expiresAt;
}
